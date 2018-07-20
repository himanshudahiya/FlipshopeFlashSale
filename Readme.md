WebView.java is main java class where we have to add js code.
To add JS, add in "onPageFinished" override method(already written).
Replace existing js. In loadurl, write "javascript:(//your js here)()"