// $Id: UseMemberTag.java,v 1.1 2004/10/26 19:49:49 niko_schmuck Exp $
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
 * Jelly tag allowing to expose a member instance 
 * to the context of its successors.
 * 
 * The member to use may either be specified by
 * the name of a variable that is lookuped in the 
 * jelly context and must be bound to an object of
 * type Member.
 * Otherwise the member may be specified by an
 * id or an adress of a sourceLocator.
 * In this case the member will be searched in 
 * the topicmap that is the current topicmap of this 
 * member.
 * 
 * The current topicmap is either specified by the
 * tmVar-property of this instance or by
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
        
        // retrieve topic
        if (member == null)
            getMember();

         doTag(member, output);
         
//        if (member== null) {
//            // failed to retrieve association
//            if (shallFailOnNonexistant())
//                throw new JellyTagException("Failed to identify member");
//
//            else if (shallAddOnNonexistant())
//                member= tmEngine.createMember(getAssociationFromContext(), getId(),
//                        getSourceLocator(), role, player);
//
//            else {
//                // set var, ignore body
//                storeObject(null);
//                return;
//            }
//        }
//
//        // set variable
//        storeObject(member);
//        
//        // process body
//        getBody().run(context, output);

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