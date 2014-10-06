Theme-Installer
===============

Multiple theme installer for cm11 themes

* Adding themes
Put your themes inside assets/files/
Their names should be theme1.apk,theme2.apk,theme3.apk and theme4.apk respectively

* Add theme icons
You can find 4 icons(theme_icon1, theme_icon2, ...) inside drawable directories. Replace them with your own icons. Dont change the names
* Add previews
There are four placeholder preview images (theme_preview1, theme_preview2, ...) inside drawable directories. Replace them with your theme previews

* Add values to strings
    <string name="current_version">1.0</string> // add your theme version( mandatory)
    <string name="app_name">Theme Installer</string> // change this to change app name
    <string name="theme_name1">Theme number one</string> // theme one name
    <string name="theme_name2">Theme number two</string> // theme two name
    <string name="theme_name3">Theme number three</string> // theme three name
    <string name="theme_name4">Theme number four</string> // theme four name 
    <string name="title_activity_theme_one_selector">Theme One</string>// theme one name
    <string name="title_activity_theme_two_selector">Theme Two</string>// theme two name
    <string name="title_activity_theme_three_selector">Theme Three</string>// theme three name
    <string name="title_activity_theme_four_selector">Theme Four</string>// theme four name
<string name="share">Share theme</string>
<string name="play_store_link">https://play.google.com/store/apps/details?id=com.b16h22.nero</string> // play store link
<string name="email_id">blahblah@gmail.com</string> // Developer email id
<string name="email_subject">Blah blah theme</string>  // Email subject 

* If you have less number of themes make sure to set the layout_weight value of unwanted layouts in activity_main.xml to "0.0"

* Colors
Every color used in the app including window background, action bar are coded here. Change them to change the visual properties 
