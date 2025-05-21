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

var jQuery = jQuery || {};
jQuery.extend = function() {
	// copy reference to target object
	var target = arguments[0] || {}, i = 1, length = arguments.length, deep = false, options;

	// Handle a deep copy situation
	if ( typeof target === "boolean" ) {
		deep = target;
		target = arguments[1] || {};
		// skip the boolean and the target
		i = 2;
	}

	// Handle case when target is a string or something (possible in deep copy)
	if ( typeof target !== "object" && !jQuery.isFunction(target) )
		target = {};

	// extend jQuery itself if only one argument is passed
	if ( length == i ) {
		target = this;
		--i;
	}

	for ( ; i < length; i++ )
		// Only deal with non-null/undefined values
		if ( (options = arguments[ i ]) != null )
			// Extend the base object
			for ( var name in options ) {
				var src = target[ name ], copy = options[ name ];

				// Prevent never-ending loop
				if ( target === copy )
					continue;

				// Recurse if we're merging object values
				if ( deep && copy && typeof copy === "object" && !copy.nodeType )
					target[ name ] = jQuery.extend( deep, 
						// Never move original objects, clone them
						src || ( copy.length != null ? [ ] : { } )
					, copy );

				// Don't bring in undefined values
				else if ( copy !== undefined )
					target[ name ] = copy;

			}

	// Return the modified object
	return target;
};


