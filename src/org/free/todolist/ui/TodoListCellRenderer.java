package org.free.todolist.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.free.todolist.model.TodoItem;

public class TodoListCellRenderer implements ListCellRenderer {
	private static URI finished;
	private static URI pending;
	private static URI prefect;
	private static URI cancelled;
	private static URI inew;
	
	static{
		try {
			inew = TodoListCellRenderer.class.getResource("schedule.png").toURI();
			finished = TodoListCellRenderer.class.getResource("green.png").toURI();
			pending = TodoListCellRenderer.class.getResource("red.png").toURI();
			prefect = TodoListCellRenderer.class.getResource("orange.png").toURI();
			cancelled = TodoListCellRenderer.class.getResource("grey.png").toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	static final Color STRIPE_COLOR = new Color(246, 246, 246);
    
    private final ListItemPanel panel;
	
	public TodoListCellRenderer(){
		panel = new ListItemPanel();
		panel.setOpaque(true);
	}
	
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if(value != null){
			panel.setTodoItem((TodoItem)value);
		}
		if(isSelected){
			adjustColors(list.getSelectionBackground(),
                    list.getSelectionForeground());
		}else{
            adjustColors(list.getBackground(),
                    list.getForeground());
            
            if ((index % 2) == 0) {
                panel.setBackground(STRIPE_COLOR);
                adjustColors(STRIPE_COLOR,
                        list.getForeground());
            }
		}
		
		return panel;
	}

    private void adjustColors(Color bg, Color fg) {
        for (Component c : panel.toAdjust) {
            c.setForeground(fg);
            c.setBackground(bg);
        }
    }
	
    
    private class ListItemPanel extends JPanel{
		private static final long serialVersionUID = -4701509142377916229L;
		private JLabel labDesc;
        private JImagePanel panelStatus;
        private List<Component> toAdjust;
        
        public ListItemPanel() {
            initComponents();
        }


        private void initComponents() {

            panelStatus = new JImagePanel();
            labDesc = new JLabel();

            setName("Form"); // NOI18N

            panelStatus.setName("panelStatus"); // NOI18N
            panelStatus.setPreferredSize(new java.awt.Dimension(28, 28));

            javax.swing.GroupLayout panelStatusLayout = new javax.swing.GroupLayout(panelStatus);
            panelStatus.setLayout(panelStatusLayout);
            panelStatusLayout.setHorizontalGroup(
                panelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 28, Short.MAX_VALUE)
            );
            panelStatusLayout.setVerticalGroup(
                panelStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 28, Short.MAX_VALUE)
            );

            labDesc.setFont(new java.awt.Font("Verdana", Font.ITALIC, 12)); // NOI18N
            labDesc.setText(""); // NOI18N
            labDesc.setName("labDesc"); // NOI18N

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(labDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
            );
            
	        toAdjust = new LinkedList<Component>();
	        toAdjust.add(this);
	        toAdjust.add(labDesc);
	        toAdjust.add(panelStatus);
        }
        
        public void setTodoItem(TodoItem item){
        	labDesc.setText("<html>"+item.getDesc()+"</html>");
        	labDesc.setToolTipText(item.getDesc());
        	String status = item.getStatus();
        	if(status.equals("new")){
        		panelStatus.setImagePath(inew);
        		panelStatus.setToolTipText("Status : new");
        	}else if(status.equalsIgnoreCase("finished")){
        		panelStatus.setImagePath(finished);
        		panelStatus.setToolTipText("Status : finished");
        	}else if(status.equalsIgnoreCase("pending")){
        		panelStatus.setImagePath(pending);
        		panelStatus.setToolTipText("Status : pending");
        	}else if(status.equalsIgnoreCase("prefect")){
        		panelStatus.setImagePath(prefect);
        		panelStatus.setToolTipText("Status : prefect");
        	}else if(status.equalsIgnoreCase("cancelled")){
        		panelStatus.setImagePath(cancelled);
        		panelStatus.setToolTipText("Status : cancelled");
        	}else {
        		panelStatus.setImagePath(cancelled);
        		panelStatus.setToolTipText("Status : unknown");
        	}
        	
        }

    }
    
}
