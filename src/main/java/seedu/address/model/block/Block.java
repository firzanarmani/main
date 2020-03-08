package seedu.address.model.block;

import java.util.UUID;
import java.util.HashMap;

public class Block {
  	// Identity field.
  	private String title;
	private String body;
  	private UUID uuid;
  
  	// Data field.
  	private HashMap<UUID, Block> children;
  
  
  	public Block(String title, String body) {
    	this.children = new HashMap<>();
      	this.title =title;
      	this.body = body;
      	this.uuid = UUID.randomUUID();
    }
    
    public String getTitle() {
        return this.title;
    }
  
    public String getBody() {
        return this.body;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void addChildren(Block block) {
        this.children.put(block.getUuid(), block);
    }

    public HashMap<UUID, Block> getChildren() {
        return this.children;
    }

    @Override
    public boolean equals(Object obj) {
        return this.uuid.equals(((Block) obj).uuid);
    }
}