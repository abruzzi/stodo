var JMenu = Java.type('javax.swing.JMenu')
var JMenuItem = Java.type('javax.swing.JMenuItem')
var ImageIcon = Java.type('javax.swing.ImageIcon')

var app = Application;

var STodoMenuItem = function(text, icon, func){
	this.menu = new JMenuItem();
	this.menu.setText(text);
	if(icon){
		this.menu.setIcon(new ImageIcon(icon));
	}
	if(func){
		this.click(func);
	}
};

STodoMenuItem.prototype.click = function(func){
	this.menu.addActionListener(
		new JavaAdapter(
			ActionListener, {
				actionPerformed : func
			}
		)
	);
};

STodoMenuItem.prototype.getMenuObject = function(){
	return this.menu;
}

function buildPluginMenu(){
	var menuPlugin = new JMenu();
	menuPlugin.setText("Plugin");
	menuPlugin.setIcon(new ImageIcon("imgs/plugin.png"));
	
	var menuItemListPlugin = new JMenuItem();
	menuItemListPlugin.setText("list plugins");
	menuItemListPlugin.addActionListener(
	new JavaAdapter(
		ActionListener, {
			actionPerformed : function(event){
				var plFrame = new JFrame("plugins list");
				var epNote = new JEditorPane();
				var s = "";
				pluginList = app.getPluginList();
				for(var i = 0; i<pluginList.size();i++){
					var pi = pluginList.get(i);
					s += pi.getName()+":"+pi.getDescription()+"\n";
				}
				epNote.setText(s);
				epNote.setEditable(false);
				plFrame.add(epNote, BorderLayout.CENTER);
				plFrame.setSize(200,200);
				plFrame.setLocationRelativeTo(null);
				plFrame.setVisible(true);
			}
		}
	));
	
	menuPlugin.add(menuItemListPlugin);
	
	return menuPlugin;	
}

function buildHelpMenu(){
	var menuHelp = new JMenu();
	menuHelp.setText("Help");
	
	var menuItemHelp = new JMenuItem();
	menuItemHelp.setText("Help");
	
	menuItemHelp.addActionListener(
	new JavaAdapter(
		ActionListener, {
			actionPerformed : function(event){
				importPackage(Packages.org.someone.dialog);
				var hDialog = new HelpDialog(null, "This is Help");
			}
		}
	));
	
	menuHelp.add(menuItemHelp);

	var mAlert = new JMenuItem();
	mAlert.setText("Alert");
	mAlert.addActionListener(new JavaAdapter(
		ActionListener,
		{
			actionPerformed : function(event){
				alert("Hello, world");
				//alert(event.toString());
			}
		}
	));
	menuHelp.add(mAlert);
	
	var menu = new STodoMenuItem("test", "imgs/plugin.png");
	menu.click(function(){
		alert("tomorrow is another day");
	});
	
	menuHelp.add(menu.getMenuObject());
	return menuHelp;
}

//this function will be invoked from java code, MainFrame...
function _customizeMenuBar_(menuBar){
	menuBar.add(buildPluginMenu());
	menuBar.add(buildHelpMenu());
}