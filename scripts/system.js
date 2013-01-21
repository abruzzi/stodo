importPackage(java.awt, java.awt.event)
importPackage(Packages.javax.swing)
importPackage(java.io)
importClass(java.lang.System)

/*
 * put all initialize code here
 */
function main(){
	var app = Application;
	var ui = app.getUI();
	
	//set look and feel to windows
	ui.setLookAndFeel("windows");
	
	//load some new scripts
	app.activePlugin("scripts/json.js");
	app.activePlugin("scripts/date.js");
	app.activePlugin("scripts/util.js");
	app.activePlugin("scripts/menubar.js");
	app.activePlugin("scripts/misc.js");
	
	app.launch();
	//loadTodosFromFile("todos.txt");
}
