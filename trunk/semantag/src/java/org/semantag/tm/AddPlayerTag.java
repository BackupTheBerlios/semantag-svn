// $Id: AddPlayerTag.java,v 1.2 2004/11/29 16:11:04 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;

/**
 * Creates a player.
 * 
 * The player is either created for the member that is 
 * explicitly specified by the <code>member</code> 
 * attribute. If no member is explicitly specified, the 
 * new player is created for the current context member.
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes allow you to specify an 
 * id / a sourceLocator 
 * for the new player. If the underlying tm-engine detects a conflict 
 * (i.e. duplicate id/ * sourceLocator) the execution of the tag will fail.
 * 
 * To specify the topic that will act as the playing topic, you use one of the 
 * various topic resolving attributes.
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
     * determines the member 
     * to which this player will be added
     */
    private void assertContext() throws JellyTagException{
        if(getMember() == null){
                String msg = "AddPlayer must be either the children of a tag";
                msg += "that exports a member to the context for its successors. ";
                msg += "Or a member must be specified explicitly via the member attribute.";
                   throw new JellyTagException(msg);
        }
    }


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
     * Add a player
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Add Player (Player "+getTopic()+" / Member: "+getMember()+")");

        // get Member
        assertContext();

        // validation
        validate();

        tmEngine.addPlayer(member, getTopic());
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
     * The topic that will be used as the playing topic. 
     * Specifying this property is exactly the same 
     * as specifying the <code>topic</code> property.
     */
    public void setPlayer(Topic topic) {
        // calls the BaseClass. This method only exists for
        // the purpose of a meaningful attributename
        super.setTopic(topic);
    }
    
    
    /**
     * The Member to which the new player will be added
     * @param mem
     */
    public void setMember(Member mem) {
        this.member = mem;
    }
}