var shell = {}
shell.history = [''];
shell.historyCursor = 0;
shell.DONE_STATE = 4;
shell.streamResponse = 0;

shell.getXmlHttpRequest = function() {
  if (window.XMLHttpRequest) {
    return new XMLHttpRequest();
  } else if (window.ActiveXObject) {
    try {
      return new ActiveXObject('Msxml2.XMLHTTP');
    } catch(e) {
      return new ActiveXObject('Microsoft.XMLHTTP');
    }
  }

  return null;
};
shell.onButtonClick = function() {
  var statement = document.getElementById('statement');
  if (this.historyCursor == this.history.length - 1) {
    this.history[this.historyCursor] = statement.value;
  }
    return this.runStatement();
};


shell.onPromptKeyPress = function(event) {
  var statement = document.getElementById('statement');
  if (this.historyCursor == this.history.length - 1) {
    this.history[this.historyCursor] = statement.value;
  }

  if (event.ctrlKey && event.keyCode == 38 /* up arrow */) {
    if (this.historyCursor > 0) {
      statement.value = this.history[--this.historyCursor];
    }
    return false;
  } else if (event.ctrlKey && event.keyCode == 40 /* down arrow */) {
    if (this.historyCursor < this.history.length - 1) {
      statement.value = this.history[++this.historyCursor];
    }
    return false;
  } else if (!event.altKey) {    
    this.historyCursor = this.history.length - 1;
    this.history[this.historyCursor] = statement.value;
  }
//
//  if (event.keyCode == 13 /* enter */ && event.shiftKey) {
//    return this.runStatement();
//  }
};

shell.doneRequest = function(req) {
  if (req.readyState == this.DONE_STATE) {
    var result = req.responseText.replace(/^\s*|\s*$/g, '');  // trim whitespace
    //document.getElementById('ajax-status').innerHTML = result;
    document.getElementById('output').value=result; // ANIRUDH: Changed this
  }
};

shell.doneResponse = function(req) {
  a=1;
  // ANIRUDH: Commented this out since we are reusing the bottom text area for something else. 
  //if (req.readyState == this.DONE_STATE) {
  //  var output = document.getElementById('output');
  //  var result = req.responseText.replace(/^\s*|\s*$/g, '');  // trim whitespace
  //  if (result != '') {
  //    output.value = result + '\n' + output.value;
  //  }
  //  mTimer = setTimeout('shell.getResponse()', 0);
  //}
};


shell.runStatement = function() {
  var output = document.getElementById('output');
  var statement = document.getElementById('statement')
  output.value = '>>> ' + statement.value + '\n' + output.value;


  var form = document.getElementById('form');
  var req = this.getXmlHttpRequest();
  if (!req) {
    document.getElementById('ajax-status').innerHTML =
        "<span class='error'>Your browser doesn't support AJAX. :(</span>";
    return false;
  }

  req.onreadystatechange = function() { shell.doneRequest(req); };

  var params = '';
  for (i = 0; i < form.elements.length; i++) {
    var elem = form.elements[i];
    if (elem.type != 'submit' && elem.type != 'button' && elem.id != 'caret') {
      var value = escape(elem.value).replace(/\+/g, '%2B');
      params += '&' + elem.name + '=' + value;
    }
  }

  req.open(form.method, form.action + '?' + params, true);
  req.setRequestHeader('Content-type',
                       'application/x-www-form-urlencoded;charset=UTF-8');
  req.send(null);


  this.history.push('');
  this.historyCursor = this.history.length - 1;
  statement.value='';

  //document.getElementById('ajax-status').innerHTML = "Sending"; // ANIRUDH: Come back to this later if required. 
// ANIRUDH: Don't know what the code below does 
//  if (this.streamResponse == 0) {
//    this.getResponse();
//    this.streamResponse = 1;
//  }

  return false;
};

shell.getResponse = function() {
  var form = document.getElementById('form');
  var req = this.getXmlHttpRequest();
  if (!req) {
    document.getElementById('ajax-status').innerHTML =
        "<span class='error'>Your browser doesn't support AJAX. :(</span>";
    return;
  }

  req.onreadystatechange = function() { shell.doneResponse(req); };

  var params = '';
  for (i = 0; i < form.elements.length; i++) {
    var elem = form.elements[i];
    if (elem.type != 'submit' && elem.type != 'button' && elem.id != 'caret') {
      var value = escape(elem.value).replace(/\+/g, '%2B');
      params += '&' + elem.name + '=' + value;
    }
  }

  req.open(form.method, "shell-response.py" + '?' + params, true);
  req.setRequestHeader('Content-type',
                       'application/x-www-form-urlencoded;charset=UTF-8');
  req.send(null);
};

