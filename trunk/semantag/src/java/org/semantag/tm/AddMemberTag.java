// $Id: AddMemberTag.java,v 1.2 2004/11/29 16:11:04 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;

/**
 * Creates a member.
 * 
 * The member is either created for the association that is 
 * explicitly specified by the <code>association</code> 
 * attribute. If no association is explicitly specified, the 
 * member is  created for the current context association.
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes allow you to specify an 
 * id / a sourceLocator 
 * for the new member. If the underlying tm-engine detects a conflict 
 * (i.e. duplicate id/ * sourceLocator) the execution of the tag will fail.
 * 
 * The first player of the member is specified via the <code>player</code> attribute. 
 * Additional player may be specified by enclosed <code>addPlayer</code>-tags.
 * 
 * The role type of the new member is specified via the <code>role</code> attribute.
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class AddMemberTag extends BaseTMTag implements ContextMember {
    
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddMemberTag.class);
  
  /**
   * The association to which the member will be added
   */
  private Association association;
  
  /**
   * The topic used as the roleSpec of the new member
   */
  private Topic role;
  
  /**
   * A topic used as the initial player of the new member
   */
  private Topic player;

  /**
   * the new member 
   */
  private Member member;
      
  
  /**
   * @return the new member, created by this tag
   */    
  public Member getMember() {
    return member;
  }
  
  /**
   * Creates a Member and adds it to the parent association
   */

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {

      if(log.isDebugEnabled())
          log.debug("Add Member (RoleSpec"+getRole()+" / Player: "+getPlayer()+")");


      // get Parent
      assertContext();
      
      // validation
      validate();
      
      // create occurrence
      member = tmEngine.createMember(association, getId(),
              getSourceLocator(), role, player);

      // set variable
      storeObject(member);

      // process body
      getBody().run(context, output);

  }
  
  /**
   * sets the Association to which the new member will be added
   */
  private void assertContext() throws JellyTagException{
      if(association == null){
          association = getAssociationFromContext();
          if(association == null){
                  String msg = "AddMember must be either the children of an object ";
                  msg += "that exports an association to the context for its successors ";
                  msg += "or a variable containig an Association must be specified via the attribute 'association'.";
                  throw new JellyTagException(msg);

          }
      }
  }
  
  protected void validate(){
      /*
       * There is nothing to do here.
       * Player and role are allowed to be 
       * null and the existance of the target association
       * is handled by assertAssociation(), 
       *
       */
      
  }
  
  
  /**
   * The Association to which the new member will be added
   */
  public void setAssociation(Association association) {
      this.association = association;
  }

  /**
   * @return the initial player of the new member
   */
  public Topic getPlayer() {
      return player;
  }
  
  /**
   * The initial player of the new member
   */
  public void setPlayer(Topic player) {
      this.player = player;
  }
  
  /**
   * @return the roleSpec of the new member
   */
  public Topic getRole() {
      return role;
  }

  /**
   * The roleSpec of the new member
   */
  public void setRole(Topic role) {
      this.role = role;
  }

}
