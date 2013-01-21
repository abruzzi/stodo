package org.free.todolist.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.free.todolist.data.DataService;
import org.free.todolist.model.TodoItem;

public class AlarmDialog extends JDialog{
	private static final long serialVersionUID = 7752112784554536906L;
	private TodoItem dataModel;
	
    public AlarmDialog(TodoItem item) {
    	dataModel = item;
        initComponents();
    }

    public void updateTodoItemStatus(String status){
    	dataModel.setStatus(status);
    	DataService ds = DataService.getInstance();
    	ds.updateItem(dataModel);
    }
    
    public void closeSelf(){
    	setVisible(false);
    }
    
    private String formatTooltip(TodoItem item){
    	StringBuffer formatted = new StringBuffer();
    	
    	formatted.append("<html>");
    	formatted.append("<b>Description : </b>").append(item.getDesc()).append(", <br/>");
    	formatted.append("<b>Status : </b>").append(item.getStatus()).append(", <br/>");
    	formatted.append("<b>Timeout : </b>").append(item.getTimeout()).append("<br/>");
    	formatted.append("</html>");

    	return formatted.toString();
    }
    
    private void initComponents(){
    	setTitle("todo item : "+dataModel.getDesc());
    	String t = formatTooltip(dataModel);
    	AlarmPanel panel = new AlarmPanel(this, t);
    	add(panel);
    }
    
    TodoItem getTodoItem(){
    	return dataModel;
    }
    
    class AlarmPanel extends JPanel{
		private static final long serialVersionUID = 8289268785003213308L;
		private AlarmDialog parent;
    	private String tip;
    	
    	public AlarmPanel(AlarmDialog dialog, String t){
    		parent = dialog;
    		tip = t;
    		initComponents();
    	}
    	
        private void initComponents() {
            radiosGroup = new javax.swing.ButtonGroup();
            labTodoItem = new javax.swing.JLabel();
            
            radioNew = new javax.swing.JRadioButton();
            radioFinished = new javax.swing.JRadioButton();
            radioPending = new javax.swing.JRadioButton();
            radioCancelled = new javax.swing.JRadioButton();
            
            btnOkay = new javax.swing.JButton();
            btnCancel = new javax.swing.JButton();

            radiosGroup.add(radioNew);
            radiosGroup.add(radioFinished);
            radiosGroup.add(radioPending);
            radiosGroup.add(radioCancelled);
            
            radioFinished.setSelected(true);

            labTodoItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            labTodoItem.setText(tip); // NOI18N
            labTodoItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            labTodoItem.setName("labTodoItem"); // NOI18N

            radioNew.setText("New"); // NOI18N
            radioNew.setName("radioNew"); // NOI18N

            radioFinished.setText("Finished"); // NOI18N
            radioFinished.setName("radioFinished"); // NOI18N

            radioPending.setText("Pending"); // NOI18N
            radioPending.setName("radioPending"); // NOI18N

            radioCancelled.setText("Cancelled"); // NOI18N
            radioCancelled.setName("radioCancelled"); // NOI18N

            btnOkay.setIcon(new ImageIcon("imgs/icon.png"));
            btnOkay.setText("Okay"); // NOI18N
            btnOkay.setName("btnOkay"); // NOI18N

            btnCancel.setIcon(new ImageIcon("imgs/cancel.gif"));
            btnCancel.setText("Cancel"); // NOI18N
            btnCancel.setName("btnCancel"); // NOI18N

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnOkay, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(labTodoItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(radioNew)
                                .addComponent(radioFinished)
                                .addComponent(radioPending)
                                .addComponent(radioCancelled)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(btnCancel)))
                    .addContainerGap(14, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(labTodoItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(radioNew)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(radioFinished)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(radioPending)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(radioCancelled)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnOkay)
                        .addComponent(btnCancel))
                    .addContainerGap())
            );
            
            btnOkay.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					String status = "new";
					if(radioNew.isSelected()){
						status = "new";
					}else if(radioFinished.isSelected()){
						status = "finished";
					}else if(radioPending.isSelected()){
						status = "pending";
					}else if(radioCancelled.isSelected()){
						status = "cancelled";
					}

					TodoItem item = parent.getTodoItem();
					item.setStatus(status);
					DataService ds = DataService.getInstance();
					ds.updateItem(item);
					
					parent.closeSelf();
				}
            });
            
            btnCancel.addActionListener(new ActionListener(){
            	public void actionPerformed(ActionEvent e){
            		parent.closeSelf();
            	}
            });
        }

        private javax.swing.JButton btnCancel;
        private javax.swing.JButton btnOkay;
        private javax.swing.JLabel labTodoItem;
        private javax.swing.JRadioButton radioCancelled;
        private javax.swing.JRadioButton radioFinished;
        private javax.swing.JRadioButton radioNew;
        private javax.swing.JRadioButton radioPending;
        private javax.swing.ButtonGroup radiosGroup;	
    }
    
    public static void main(String[] args){
    	TodoItem item = new TodoItem();
    	item.setId("id");
    	item.setDesc("desc");
    	item.setNote("note");
    	item.setPeriod("none");
    	item.setStatus("new");
    	item.setTimeout("timeout");
    	item.setType("node");
    	
    	AlarmDialog dialog = new AlarmDialog(item);
    	dialog.setSize(386, 174);
    	dialog.setVisible(true);
    }
}
