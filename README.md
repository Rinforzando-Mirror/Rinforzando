<a href="https://codeberg.org/Rinforzando/Rinforzando" target="_blank" rel="noopener noreferrer"><img alt="codeberg" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/codeberg_vector.svg"></a> <img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg"> <img alt="forge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/forge_vector.svg"> 

[Join the discord!](https://discord.gg/Qu2DGVuqW7)
## Rinforzando
*A PvP-focused AltoClef fork for newer versions.*

> **⚠️ Work in Progress**
> 
> Rinforzando is currently in a very early stage of active development. Because it is a **work in very progress**, there are **no official builds or wiki pages available yet**. Stay tuned for updates!

**Rinforzando** is a utility-focused modification designed to optimize bot pathfinding and combat mechanics for modern Minecraft versions. This project is built directly on top of [MarvionKirito's AltoClef](https://github.com/MarvionKirito/altoclef) and parts of [MiranCZ's fork](https://github.com/MiranCZ/altoclef), inheriting its stability improvements.

## Features

### Combat Oriented
Adjusts basic automation routines to better account for PvP environments and encounter mechanics on updated releases.

### Version Support
Built to push past the limits of older releases, keeping pace with and supporting newer Minecraft versions than other AltoClef forks out there.

### 3D Octree System
Replaces voxel-based navigation with a hierarchical 3D octree structure, enabling more efficient spatial partitioning, and faster path queries.

### Lazy Theta\* & HPA\* Pathfinding
Uses lazy evaluation. Instead of constantly checking if a straight line is blocked at every single step, it handles massive maps efficiently by breaking them down into smaller chunks.

<p align="center" style="font-size: 0.8em; color: gray;">
  Not affiliated with Mojang Synergies AB.<br>
  © 2026 Rinforzando Development.
</p>
