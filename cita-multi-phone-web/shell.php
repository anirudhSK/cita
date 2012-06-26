<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title> Code In The Air </title>
<script type="text/javascript" src="shell-async.js?seed=<?php printf("%s", uniqid()); ?>"> 
</script>
<link rel="stylesheet" type="text/css" href="shell.css" /> 
</head>

<body>

<p>  <a href="api.html" target=”_blank”>APIs</a>
</p>


<p id="ajax-status">Write a CITA script</p>

<form id="form" action="./shell-request.py" method="get">
  <nobr>
  <!-- <input type="text" class="prompt" id="user" name="device" value="leninS" />  -->
  <input type="hidden" class="prompt" id="task" name="task" value="<?php printf("%s", uniqid()); ?>" />
  </nobr>
  <br>
  <nobr>
  <textarea class="prompt" id="caret" readonly="readonly" rows="15"
            onfocus="document.getElementById('statement').focus()">&gt;&gt;&gt;</textarea> 
  <textarea class="prompt" name="statement" id="statement" rows="15" spellcheck='false'
            onkeypress="return shell.onPromptKeyPress(event);"></textarea>
  <br>
  <button type="button" onclick="return shell.onButtonClick()">Create and Run </button>
  <br><br>
  </nobr>

  <input type="hidden" name="session" value="<?php printf("%s", uniqid()); ?>" />
  <input type="submit" style="display:none" />
</form>


<textarea id="output" rows="28" readonly="readonly">
</textarea>



<script type="text/javascript">
document.getElementById('statement').focus();
</script>

</body>
</html>

