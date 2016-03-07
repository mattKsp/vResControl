# vResControl

Android app for basic control of Resolume VJ Software.

Written by M.Standish (mattKsp) 2016. (Copyright to follow.. but as this is available to anyone for free, i don't care that much, just don't steal my name).

This software is provided on an "AS-IS-BASIS".

Uses JavaOSC library by C.Ramakrishnan (updated by hoijui).
* http://www.illposed.com/software/javaosc.html
* https://github.com/hoijui/JavaOSC

## About

This project is an exercise in implementing OSC communication in an Android app.

Tested with Resolume Avenue 4 demo.
* https://resolume.com

## Notes

* This app cannot currently cope with bundles. (Make sure Bundles are turned OFF in Resolume OSC Preferences).
* A few things left to fix.
* Not optimised for tablets yet.

The layout is intentionally minimal. There is not much screen space on a mobile device, 
and there are not many different ways to re-arrange things in grids.

Communication is done via OSC/UDP over WIFI/LAN. Resolume accepts input from many devices at once, 
this enables several people to VJ together pretty easily (..but unfortunately Resolume can only talk back to 1 device).
