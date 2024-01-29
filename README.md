The game
==========
"A game is a voluntary effort to overcome unnecessary obstacles." Bernard Suits

This game consists in a series of interactions with a text interface capable of:
 - describing locations and objects
 - understading an extremely simplified version of the natural language (english), which the player may use to
   move between locations and manipulate objects, in order to advance toward game ending
The purpose of the game is, as a matter of fact, to reach one of the possible endings (not defined at the moment): 
e.g. the elimination of all enemies, the conquest of a treasure or of liberty, the death of the main character.


Entities in the game
======================
In the game only these entities are "present": things, locations and characters.

 - Things (objects, but "Object" is a reserved word in Java...)
 	- Characterized by a unique name (start location + simple name) and a set of possible statuses
	- Things may or may not be operated upon with verbs (and possibly other objects, or only in certain locations)
		- An apple might be taken (unless it's hanging from a far above, unreachable branch...)
		- A door can be opened / closed (locked / unlocked with the proper key) but not taken...
	- Associated, at any given game time, with:
		- its current location
		- its current status
	- Thing status properties
		- description
		- canonical name and synonyms
		- actions, which may result in status transition of the thing itself, other things and locations as well
		- can be taken + can be dropped (special 1-Actions)
	- Actions
		- 1-Actions (see)
			- thing=>thing { 1-action } x { thing status } x { location status | * } x { thing status } x { new thing status }
			- thing=>location { 1-action } x { thing status } x { location status | * } x { location status } x { new location status }

 - Locations
	- Rooms, places, ..., and inventories of the characters
	- Characterized by a name (not really required on-game, but useful for in-code reference) and a set of possible statuses
	- Associated, at any given game time, with
		- currently contained things and characters
		- its current status 
	- Location status properties
		- description
		- set of connections with other locations (north / south / east / ... 0-actions)

 - Characters
	- A human (or not) being, living (or magically animated) or dead
	- Characterized by a name, a description
	- Associated, at any given game time, with his/her current location
	- Characterized by possessing an inventory (collection of things following him/her)
	- A character may be the Main Character (MC) impersonated by the player, or a Non-Playing Character (NPC)


The language which can be used
================================
Parts of discourse:
 - Commands
 - Actions (see below)
 - Names
 - Prepositions

Only some specific types of utterances are recognized: Commands (off-game) and 0/1/2-Actions (on-game).

 - Commands
	- save
	- load
	- quit

 - 0-Actions (without a complement)
	- east
	- up
 	- look

 - 1-Actions (with one complement)
	- examine room
 	- take gem
	- open door

 - 2-Actions (with with complements)
	- unlock door with key
	- put gem in socket
	- drop box on table

Some objects may be identied by more than one word:
	- small key
	- red button
	- green button
	- metal bar

Only particular combinations of verbs, things and complements work:
	- open gem => ??? (gem cannot be open-ed)
	- examine gem with glass => ??? (a gem can be examine-d but this is a 1-Action)
	- unlock door with gem => ??? (door can be unlock-ed but not with a gem)
	- start fire => it works only if you:
		- possess (inventory) flint, steel
		- a wood pile is present with some fire starter on it (put f.s. on ...)


Evolution of entities during the game
=======================================
The status of several entities evolve during the game, as consequence of actions.

 - Locations
 	- May acquire or loose connections to other locations
		- an unlocked door
		- barred road

 - Things
	- Things may change
		- You see a door.
		- examine door
			=> A simple wooden door

		- paint door with red paint
		- examine door
		 	=> A simple wooden door, painted in red
	- Things may spawn or de-spawn
		- a destroyed object which does not exist any longer
		- an object is replaced by another one (gold chunk => forge ring with gold chunk => golden ring)

 - Characters
	- Some circumstances / actions operated on him / actions operated on particular things (in current location or not) may result in:
		- the death of the character
		- other consequences

Version 0
===========
 - Entities
 	- Things: NO
	- Locations: YES
		- Rooms, places, ... (not inventories of characters for now)
		- Characterized by a name (not really required on-game, but useful for in-code reference) and a description 
		- Can be connected to other locations via north / south / east / ... actions
		- Location status: NO
	- Characters: YES (main only)
		- Characterized by a name, a description
		- Associated, at any given game time, with his/her current location
		- Inventory: NO
 - Language
 	- Commands: YES
		- quit
		- save: NO
		- load: NO
 	- Actions (without a complement): YES - only some 0-actions
		- east, ...
		- up, down
		- look: NO
	- 1-Actions (with one complement): NO
	- 2-Actions (with with complements): NO
	- Things / objects / names: NO
	- Prepositions: NO


Version 1
===========
 - Entities
 	- Things: YES
		- thing => thing actions: YES
			- change visibility: NO
			- change location: NO
		- thing => location actions: YES
			- change description: YES
			- change directions: YES
	- Characters: (main only)
		- Inventory: NO
	- Locations:
		- Status: YES
 - Language
 	- Commands:
		- save: NO
		- load: NO
 	- 0-Actions (without a complement):
		- look
		- examine room: NO
	- 1-Actions (with one complement): YES
		- examine
		- open
		- close
	- 2-Actions (with with complements): NO
	- Prepositions: NO

Version 2
===========
 - Entities
 	- Things:
		- visibility
		- takeability
		- droppability
		- readability
	- Characters: (main only)
		- Inventory: YES
 - Language
 	- Commands:
		- save: YES
		- load: YES
 	- 0-Actions
		- inventory
	- 1-Actions
		- (special action) examine <location name>
		- (special action) take
		- (special action) drop
		- consequences of special actions
		- read

Version 3
===========
 - Language
	- ability to recognize words containing spaces ("red button"): YES
 - Entities
	- Character
		- add character status
		- add actions with consequences on character status
    	- Character status may contain:
		  - a list of possibilities (both positive and negative)
	- Locations
		- Location status and character status may contain:
		  - a list of possibilities (both positive and negative)
	- Things
		- thing status and character status may contain:
		  - a list of possibilities (both positive and negative)
    - Location possibilities > Thing possibilities > Character possibilities
	  BUT
	  If the possibility is negated, the reason shown to the user must be given in reverse order: if a location does not allow to fly (E.g. low ceiling) AND the character is not able to fly, the feedback must be "you cannot fly" and not "the ceiling is low".

Version 4
===========
 - Language
	- Support 2-actions (VERB COMPLEMENT1 PREPOSITION COMPLEMENT2)
	- Setting a complement or supplement as inventory-only or location-only
 - Entities
	- All
		- Consequences of 2-actions
		- Change of location in consequences for things & characters
 - Config
	- New syntax based on "phrase clauses"
	- Error messages
