// $Id: UseMemberTag.java,v 1.3 2004/12/09 21:19:58 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapObject;

/**
* Retrieves a Member instance and sets it as the
* context-member for nested tags.
* 
* The <code>var</code>-attribute allows to store the member in
* a variable in order to use it elsewhere in the script.
* 
* The nonexistant - attribute triggers what will happen
* if the specified member could not be found. 
* 
* @jelly
*  name="useMember"
* 
* @jelly.nested 
*  name="addPlayer" 
*  desc="adds a role-playing topic to this member" 
*  required="no"
* 
* 
* @author Niko Schmuck
* @author cf
*/
public class UseMemberTag extends BaseUseTag implements ContextMember
        {
    
    // The Log to which logging calls will be made. 
    private static final Log log = LogFactory.getLog(UseMemberTag.class);


    // member to which this tag refers to
    private Member member;

    /**
     * The topic used as the roleSpec of the new member
     * Ignored, if this tag does not lead to the creation
     * of a new member
     */
    private Topic role;
    
    /**
     * A topic used as the initial player of the new member
     * Ignored, if this tag does not lead to the creation
     * of a new member
     */
    private Topic player;

    
    
    /**
     * Resolves the member that this Tag refers to.
     * 
     * @return
     * @throws JellyTagException
     */
    public Member getMember() throws JellyTagException {

        if (member != null)
            return member;

        TopicMapObject m = super.resolve();
        if (m != null && !(m instanceof Member)) {
            throw new JellyTagException("Failed to identify member. Found "+m);
        }
        member = (Member)m;
        return member;

    }

    
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Using Member (ID "+getId()+" / SL: "+getSourceLocator()+")");
        
        // retrieve member
        if (member == null)
            getMember();

         doTag(member, output);
 ;

    }

    /**
     * Called from the superclass, when this tag must add a new object to the topicmap
     */
    protected TopicMapObject createTMO() throws JellyTagException {
        member= tmEngine.createMember(getAssociationFromContext(), getId(),
                getSourceLocator(), role, player);
        return member;
    }

    
    /**
     * @return the initial player of the new member.
     * Ignored, if this tag does not lead to the creation
     * of a new member
     */
    public Topic getPlayer() {
        return player;
    }
    
    /**
     * sets the initial player of the new member
     * Ignored, if this tag does not lead to the creation
     * of a new member
     */
    public void setPlayer(Topic player) {
        this.player = player;
    }
    
    /**
     * @return the roleSpec of the new member
     * Ignored, if this tag does not lead to the creation
     * of a new member
     */
    public Topic getRole() {
        return role;
    }

    /**
     * sets the roleSpec of the new member
     * Ignored, if this tag does not lead to the creation
     * of a new member
     */
    public void setRole(Topic role) {
        this.role = role;
    }

    /**
     * sets the mamber that this tag shall use
     * @param m
     */
    public void setMember(Member m){
        member= m;
    }

}