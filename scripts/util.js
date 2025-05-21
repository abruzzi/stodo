const JOptionPane = Java.type("javax.swing.JOptionPane");

//format the todoitem object to String
function formatTooltip(item){
    	var formatted = new java.lang.StringBuffer();
    	
    	formatted.append("<html>");
    	formatted.append("<b>Description : </b>").append(item.getDesc()).append(", ");
    	formatted.append("<b>Status : </b>").append(item.getStatus()).append(", ");
    	formatted.append("<b>Timeout : </b>").append(item.getTimeout());
    	formatted.append("</html>");

    	return formatted.toString();
}

//show the message as a message dialog
function alert(message){
	JOptionPane.showMessageDialog(
			null, 
			message, 
			"Alert", 
			JOptionPane.INFORMATION_MESSAGE);
}

//just print a message on the console
function log(message){
	var prefix = "[stodo log trace] ";
	if(!println){
		println = function(message){
			print(prefix+message+"\n");
		}
	}
	println(prefix+message);
}

/*
 * parse time by English style of description, like:
 * tomorrow,
 * today +3 hours,
 * yesterday,
 * last April,
 * +10 min,
 * etc
 */
function parseTimeout(input){
	input = input || "now +5 min";
	var standard = Date.parse(input);
	standard = standard || new Date();
	var date = standard.toString("yyyy-MM-dd HH:mm:ss");
	return date;
}

//parseTimeout("tomorrow +3 hours");
//parseTimeout("today +30 min");
//parseTimeout("1/7 10 pm");
