// $Id: AddPlayerTag.java,v 1.1 2004/09/19 15:35:53 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Tag to add a topic as a player to a Member.
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class AddPlayerTag extends BaseTopicReferenceTag {
    
    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddPlayerTag.class);


    /**
     * The Member to which the player is added..
     */
    private Member member;


    /**
     * validates that both <code>parent</code> and <code>type</code> are
     * specified.
     */
    private void validate() throws MissingAttributeException, JellyTagException {
        if (getTopic() == null) {
            String msg = "AddPlayer requires that a Topic is referenced as the 'player'. ";
            msg +=" This is done either by setting the attribute 'player' to a topic or by using ";
            msg +="one of the various attributes of the TopicReference.";
            throw new MissingAttributeException(msg);
        }

        if (getMember() == null) {
            String msg = "AddPlayer requires attribute 'member' set to a Member";
            throw new MissingAttributeException(msg);

        }

    }

    /**
     * @return the member to which the player will be added.
     */
    public Member getMember() throws JellyTagException {

        if (member != null)
            return member;

        member = getMemberFromContext();

        return member;

    }

    /**
     * Adds a type to the parent specified
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Add Player (Player "+getTopic()+" / Member: "+getMember()+")");

        validate();

        tmEngine.addPlayer(getMember(), getTopic());
    }

    /**
     * @return the topic that will be added as
     * a player to the member
     */
    public Topic getPlayer() throws JellyTagException{
        // calls the BaseClass. This method only exists for
        // the purpose of a meaningful attributename
        return getTopic();
    }

    /**
     * sets the theme that will be added to the set of
     * themes that constitutes the scope of the scopedObject
     */
    public void setPlayer(Topic theme) {
        // calls the BaseClass. This method only exists for
        // the purpose of a meaningful attributename
        super.setTopic(theme);
    }
    
    
    /**
     * set the Member to whoom the
     * player will be added
     * @param mem
     */
    public void setScopedObject(Member mem) {
        this.member = mem;
    }
}