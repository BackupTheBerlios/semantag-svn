package net.sf.semantag.tm;


/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public abstract class BaseUseTag extends BaseTMTag {

    public final static String NE_IGNORE_BODY = "ignore";
    public final static String NE_ADD = "add";
    public final static String NE_FAIL = "fail";
    
    public final static String NE_DEFAULT= NE_ADD;
    
    
    private String nonexistant = NE_ADD;
    
    
    /**
     * 
     */
    public BaseUseTag() {
        super();
    }

    public boolean shallFailOnNonexistant(){
        return nonexistant.equals(NE_FAIL);
    }

    public boolean shallAddOnNonexistant(){
        return nonexistant.equals(NE_ADD);
    }
    
    public boolean shallIgnoreOnNonexistant(){
        return nonexistant.equals(NE_IGNORE_BODY);
    }
    
    
    public String getNonexistant() {
        return nonexistant;
    }
    
    public void setNonexistant(String nonexistant) {
        if(nonexistant == null) {
            this.nonexistant = NE_DEFAULT;
        }
        else if(nonexistant.equalsIgnoreCase(NE_FAIL)){
            this.nonexistant = NE_FAIL; 
        }
        else if(nonexistant.equalsIgnoreCase(NE_ADD)){
            this.nonexistant = NE_ADD; 
        }
        else if(nonexistant.equalsIgnoreCase(NE_IGNORE_BODY)){
            this.nonexistant = NE_IGNORE_BODY; 
        }
        else{
            this.nonexistant = NE_DEFAULT; 
        }
    }
        
}
