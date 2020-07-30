# How does it work?
This document provides some high-level insights into the inner workings of the library to make navigating the source code easier.

## Modules
The repository contains the following modules:

<img src="modules.png" width="80%" />

The modules in the **UI** column are exposed for public usage. The ones in the **Demo** column are just for demonstration.

The **common-base** module as well as all modules prefixed with **log** are pure Kotlin modules that don't depend on the Android SDK.

Modules prefixed with **beagle** are deprecated.

## Coming soon…
Concepts to describe:
 - RecyclerView adapter implementation with dynamically updated delegates
 - OverlayFragment