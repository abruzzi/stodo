/**
 * Copyright (c) 2006, Sun Microsystems, Inc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following 
 *     disclaimer in the documentation and/or other materials provided 
 *     with the distribution.
 *   * Neither the name of the TimingFramework project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.free.todolist.ui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.TooManyListenersException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author sky
 */
public class JImagePanel extends JPanel {
    private boolean showHintOnEmptyImage;
    private Image image;
    private URI imagePath;
    private boolean editable;
    private Image cachedImage;
    private Image dragImage;

    static {
        UIManager.getDefaults().addResourceBundle("passwordstore.swingx.resources.strings");
    }
    
    public JImagePanel() {
        setBackground(Color.WHITE);
        setOpaque(true);
        showHintOnEmptyImage = true;
        updateHintIfNecessary();
    }
    
    public void setShowHintOnEmptyImage(boolean value) {
        boolean oldValue = showHintOnEmptyImage;
        showHintOnEmptyImage = value;
        firePropertyChange("showHintOnEmptyImage", oldValue, value);
    }
    
    public boolean getShowHintOnEmptyImage() {
        return showHintOnEmptyImage;
    }
    
    public void setEditable(boolean editable) {
        if (editable != this.editable) {
            this.editable = editable;
            if (editable) {
                setDropTarget(new DropTarget());
                try {
                    getDropTarget().addDropTargetListener(new DropTargetHandler());
                } catch (TooManyListenersException ex) {
                }
                enableEvents(MouseEvent.MOUSE_EVENT_MASK);
            } else {
                setDropTarget(null);
            }
            updateHintIfNecessary();
            firePropertyChange("editable", !editable, editable);
        }
    }
    
    public boolean isEditable() {
        return editable;
    }
    
    public final void setImage(Image image) {
        setImage0(image);
        setImagePath0(null);
    }
    
    private void setImage0(Image image) {
        Image oldImage = this.image;
        this.image = image;
        clearCachedImage();
        firePropertyChange("image", oldImage, image);
        revalidate();
        repaint();
        updateHintIfNecessary();
    }
    
    public final Image getImage() {
        return image;
    }
    
    protected void setDragImage(Image image) {
        Image oldImage = this.dragImage;
        this.dragImage = image;
        clearCachedImage();
        updateHintIfNecessary();
        firePropertyChange("dragImage", oldImage, image);
    }
    
    protected Image getDragImage() {
        return dragImage;
    }
    
    public final void setImagePath(URI path) {
        if (path == null) {
            setImage0(null);
        } else {
            setImage0(ImageCache.getInstance().getImage(this, path, 0, 0));
        }
        setImagePath0(path);
    }
    
    private void setImagePath0(URI path) {
        URI oldPath = this.imagePath;
        this.imagePath = path;
        firePropertyChange("imagePath", oldPath, imagePath);
    }
    
    public final URI getImagePath() {
        return imagePath;
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = getImageToDraw();
        if (image != null) {
            Point loc = getImageLocation();
            g.drawImage(image, loc.x, loc.y, this);
        }
    }
    
    private Image getImageToDraw() {
        if (cachedImage == null) {
            cachedImage = createCachedImage();
        }
        return cachedImage;
    }
    
    private Image createCachedImage() {
        Image image = getDragImage();
        boolean isDrag = true;
        if (image == null) {
            image = getImage();
            isDrag = false;
        }
        if (image != null) {
            int iw = image.getWidth(this);
            int ih = image.getHeight(this);
            if (iw > 0 && ih > 0) {
                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                float aspectRatio = (float)image.getWidth(this) /
                        (float)image.getHeight(this);
                int targetWidth;
                int targetHeight;
                if (iw > ih) {
                    targetWidth = w;
                    targetHeight = (int)(targetWidth / aspectRatio);
                    if (targetHeight > h) {
                        targetHeight = h;
                        targetWidth = (int)(aspectRatio * targetHeight);
                    }
                } else {
                    targetHeight = h;
                    targetWidth = (int)(aspectRatio * targetHeight);
                    if (targetWidth > w) {
                        targetWidth = w;
                        targetHeight = (int)(targetWidth / aspectRatio);
                    }
                }
                if (targetWidth != iw || targetHeight != ih) {
                    Image cachedImage;
                    if (isDrag) {
                        if (getGraphicsConfiguration() != null) {
                            cachedImage = getGraphicsConfiguration().
                                    createCompatibleImage(targetWidth, targetHeight,
                                    Transparency.TRANSLUCENT);
                        } else {
                            cachedImage = new BufferedImage(targetWidth,
                                    targetHeight, BufferedImage.TYPE_INT_ARGB);
                        }
                        Graphics imageG = cachedImage.getGraphics();
                        if (imageG instanceof Graphics2D) {
                            ((Graphics2D)imageG).setComposite(AlphaComposite.
                                    getInstance(AlphaComposite.SRC_OVER, .3f));
                            ((Graphics2D)imageG).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                        }
                        imageG.drawImage(image, 0, 0, targetWidth, targetHeight,
                                0, 0, iw, ih, this);
                        imageG.dispose();
                    } else {
                        cachedImage = ImageCache.getInstance().getImage(this, 
                                image, targetWidth, targetHeight);
                    }
                    return cachedImage;
                }
                return image;
            }
        }
        return null;
    }
    
    private Point getImageLocation() {
        Image image = getImageToDraw();
        Insets insets = getInsets();
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        int iw = image.getWidth(this);
        int ih = image.getHeight(this);
        return new Point(insets.left + (w - iw) / 2,
                insets.top + (h - ih) / 2);
    }
    
    private void clearCachedImage() {
        cachedImage = null;
        repaint();
    }
    
    public void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);
        clearCachedImage();
    }
    
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if (img == getImage()) {
            return super.imageUpdate(img, infoflags, x, y, w, h);
        }
        return false;
    }
    
    public void setBorder(Border border) {
        super.setBorder(border);
        clearCachedImage();
    }
    
    protected void showChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileNameExtensionFilter(
                "image", "jpg", "gif", "jpeg", "png"));
        URI imageURI = getImagePath();
        if (imageURI != null && imageURI.getScheme().equals("file")) {
            chooser.setSelectedFile(new File(imageURI));
        }
        chooser.setMultiSelectionEnabled(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            URI uri = file.toURI();
            setImagePath(uri);
        }
    }
    
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if (!e.isConsumed() && e.getClickCount() == 1) {
            showChooser();
        }
    }

    private void updateHintIfNecessary() {
        Component hint = getHintComponent();
        if (getImage() == null && getDragImage() == null &&
                getShowHintOnEmptyImage() && isEditable()) {
            if (hint == null) {
                hint = createHintComponent();
                setLayout(new BorderLayout());
            }
            add(hint, BorderLayout.SOUTH);
            revalidate();
            repaint();
        } else if (hint != null) {
            remove(hint);
            revalidate();
            repaint();
        }
    }
    
    private Component getHintComponent() {
        if (getComponentCount() > 0) {
            return getComponent(0);
        }
        return null;
    }

    private Component createHintComponent() {
        // PENDING: this is a hack, should be a text area or something that
        // can wrap
        JLabel label = new JLabel(getLabelText());
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.LIGHT_GRAY);
        label.setBorder(new EmptyBorder(4, 4, 4, 4));
        return label;
    }
    
    private String getLabelText() {
        String text = UIManager.getString("ImagePanel.clickText");
        if (text == null) {
            text = "Click or Drop to Set";
        }
        return text;
    }
    
    
    private class DropTargetHandler implements ActionListener,
            DropTargetListener {
        private URI dragURI;
        private Timer dragTimer;
        private boolean validDragImage;
        
        public void dragEnter(DropTargetDragEvent e) {
            dragURI = null;
            stopTimer();
            validDragImage = false;
            if (e.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                try {
                    List<File>files = (List<File>) e.getTransferable().
                            getTransferData(DataFlavor.javaFileListFlavor);
                    if (files.size() == 1) {
                        File file = files.get(0);
                        dragURI = file.getCanonicalFile().toURI();
                        updateDragImage();
                    }
                } catch (IOException ex) {
                } catch (UnsupportedFlavorException ex) {
                }
            }
        }
        
        public void dragOver(DropTargetDragEvent e) {
        }
        
        public void dropActionChanged(DropTargetDragEvent e) {
        }
        
        public void dragExit(DropTargetEvent e) {
            setDragImage(null);
            stopTimer();
        }
        
        public void drop(DropTargetDropEvent e) {
            if (dragURI != null && !validDragImage) {
                updateDragImage();
            }
            boolean accepted = false;
            if (validDragImage) {
                setImage0(getDragImage());
                setDragImage(null);
                setImagePath0(dragURI);
                accepted = true;
            }
            if (accepted) {
                e.acceptDrop(DnDConstants.ACTION_COPY);
            } else {
                e.rejectDrop();
            }
        }
        
        public void actionPerformed(ActionEvent e) {
            updateDragImage();
        }
        
        private void updateDragImage() {
            File file = new File(dragURI);
            if (file.length() > 0) {
                try {
                    ImageIcon icon = new ImageIcon(dragURI.toURL());
                    Image image = icon.getImage();
                    if (image.getWidth(JImagePanel.this) > 0) {
                        stopTimer();
                        setDragImage(image);
                        validDragImage = true;
                    } else if (dragTimer == null) {
                        startTimer();
                    }
                } catch (MalformedURLException ex) {
                    validDragImage = true;
                    stopTimer();
                }
            } else if (dragTimer == null) {
                startTimer();
            }
        }
        
        private void startTimer() {
            dragTimer = new Timer(50, this);
            dragTimer.setRepeats(true);
            dragTimer.start();
        }
        
        private void stopTimer() {
            if (dragTimer != null) {
                dragTimer.stop();
                dragTimer = null;
            }
        }
    }
}
