## Overall
![image](https://github.com/Leekyliu/Superleekyo/blob/master/src/main/resources/%E6%88%AA%E5%B1%8F2019-11-15%E4%B8%8B%E5%8D%887.13.15.png)

![image](https://github.com/Leekyliu/Superleekyo/blob/master/src/main/resources/%E6%88%AA%E5%B1%8F2019-11-15%E4%B8%8B%E5%8D%887.13.48.png)

![image](https://github.com/Leekyliu/Superleekyo/blob/master/src/main/resources/%E6%88%AA%E5%B1%8F2019-11-15%E4%B8%8B%E5%8D%887.14.34.png)
### Coding Style
<a href="https://oracle.com/technetwork/java/codeconventions-150003.pdf">Oracle</a>

### Location of the Configuration file
src/main/resources/level_1.json

### Description of the Configuration file
The config file has the hero as its own JSONObject.
The immovable, movable and enemy entities are all stored in
three separate JSON arrays. Each entity that belongs to an
array requires attributes specific to that array. This facilitates
the creation process and allows you to add a new entity anywhere in
its corresponding JSON array.

## Acknowledgements
<a href="https://opengameart.org/content/top-down-2d-metal-box">Block png</a><br>

## Gengeral 
1. To run the project use the comandline 
    gradle build
    gradle run
    (The java version should be 11+)
2. The cloud will move in a move back and forth at the specified location    
3. Use → ← ↑ to control the character move right left and jump
## About Save & Load
You can use mouse to click the save & load button
Or you can use key Q to save and key W to load
