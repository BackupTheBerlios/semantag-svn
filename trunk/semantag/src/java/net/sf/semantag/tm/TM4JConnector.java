package net.sf.semantag.tm;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderException;
import org.tm4j.topicmap.source.SerializedTopicMapSource;
import org.tm4j.topicmap.source.TopicMapSource;

/**
 * Class that assembles methods to manage TopicMapObjects in a distinct engine.
 * 
 * 
 * 
 * 
 * 
 * @author cf
 * @version 0.1, created on 07.09.2004
 */
public class TM4JConnector {

    /**
     * Creates a Topic in the given TopicMap.
     * 
     * If specified the topic is created with the given id and the given
     * sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return @throws
     *         JellyTagException as a wrapper around the various exceptions that
     *         may by thrown while creating a topic
     */
    protected Topic createTopic(final TopicMap tm, String id,
            String sourceLocator) throws JellyTagException {

        // define a creator that creates a Topic
        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                return tm.createTopic(id);
            }
        };

        return (Topic) createTMO(tm, c, id, sourceLocator);
    }

    
    /**
     * Adds a type to tmo. <br>
     * tmo must be an instance of either topic, association
     * or occurrence. In all other cases, 
     * an IllegalStateException is be thrown.
     * 
     * @param tmo
     * @param type
     * @throws JellyTagException to wrap a PropertyVetoException
     * that might be thrown by tm4j while adding the type
     * @throws IllegalStateException if tmo is of an unsupported type
     */
    public void addType(TopicMapObject tmo, Topic type)
            throws JellyTagException {

        try {
            if (tmo instanceof Topic) {
                ((Topic) tmo).addType(type);
            } else if (tmo instanceof Association) {
                ((Association) tmo).setType(type);
            } else if (tmo instanceof Occurrence) {
                ((Occurrence) tmo).setType(type);
            } else {
                throw new IllegalStateException(
                        "Expected typeable object but got " + tmo);
            }
        } catch (PropertyVetoException e) {
            throw new JellyTagException(e);
        }

    }

    /**
     * Creates an Association in the given TopicMap.
     * 
     * If specified, the association is created with the given id and the given
     * sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return a newly created association
     * @throws JellyTagException
     *             as a wrapper around the various exceptions that may by thrown
     *             while an association is created
     */
    protected Association createAssociation(final TopicMap tm, String id,
            String sourceLocator) throws JellyTagException {

        // define a creator that creates an Association
        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                return tm.createAssociation(id);
            }
        };

        return (Association) createTMO(tm, c, id, sourceLocator);
    }

    /**
     * Creates an Occurrence for the given Topic.
     * 
     * If specified, the occurrence is created with the given id and the given
     * sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return a newly created occurrence
     * @throws JellyTagException
     *             as a wrapper around the various exceptions that may by thrown
     *             while an occurrence is created
     */
    protected Occurrence createOccurrence(final Topic t, final String data, final String address, 
            String id,
            String sourceLocator) throws JellyTagException {

        // define a creator that creates an Occurrence
        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                Occurrence occ = t.createOccurrence(id);
                if(data != null){
                    occ.setData(data);
                }
                else {
                    occ.setDataLocator(createLocator(address, t));
                }
                return occ;
            }
        };

        return (Occurrence) createTMO(t.getTopicMap(), c, id, sourceLocator);
    }

    /**
     * Creates an BaseName for the given Topic.
     * 
     * If specified, the basename is created with the given id and the given
     * sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return a newly created basename
     * @throws JellyTagException
     *             as a wrapper around the various exceptions that may by thrown
     *             while a basename is created
     */
    public BaseName createBasename(final Topic t, String name, 
            String id, String sourceLocator) throws JellyTagException {
        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                return t.createName(id);
            }
        };

        BaseName bn = (BaseName) createTMO(t.getTopicMap(), c, id,
                sourceLocator);
        try {
            //set the name
            bn.setData(name);
        } catch (PropertyVetoException e) {
            throw new JellyTagException(
                    "While setting name data of new basename to " + name, e);
        }

//        if (theme != null) {
//            try {
//                //set the theme
//                if (theme != null)
//                    bn.addTheme(theme);
//            } catch (PropertyVetoException e) {
//                throw new JellyTagException(
//                        "While adding a theme to new basename (" + theme + ")",
//                        e);
//            }
//        }
        return bn;
    }

    
    /**
     * Creates an BaseName for the given Topic.
     * 
     * If specified, the basename is created with the given id and the given
     * sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return a newly created basename
     * @throws JellyTagException
     *             as a wrapper around the various exceptions that may by thrown
     *             while a basename is created
     */
    public Member createMember(final Association a, String id,
            String sourceLocator, final Topic role, final Topic player) throws JellyTagException {

        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                Member mem =  a.createMember(id);
                if(role != null) mem.setRoleSpec(role);
                if(player != null) mem.addPlayer(player);
                return mem;
            }
        };

        return (Member) createTMO(a.getTopicMap(), c, id, sourceLocator);
    }

    /**
     * Creates an BaseName for the given Topic.
     * 
     * If specified, the basename is created with the given id and the given
     * sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return a newly created basename
     * @throws JellyTagException
     *             as a wrapper around the various exceptions that may by thrown
     *             while a basename is created
     */
    public TopicMapObject createTMO(TopicMap tm, TMOCreator tmoCreator,
            String id, String sourceLocator) throws JellyTagException {

        // Check the sourceLocator first.
        // Otherwise the topic map object would have been already
        // created, before we realize a potential
        // duplication of sourceLocators
        Locator sl = makeSourceLocator(tm, sourceLocator);

        try {
            TopicMapObject tmo = tmoCreator.create(id);
            if (sl != null) {
                tmo.addSourceLocator(sl);
            }
            return tmo;
        } catch (Exception e) {
            throw new JellyTagException("While creating TopicMapObject (ID: "
                    + id + "/SourceLocator: " + sourceLocator + ")", e);
        }
    }

    /**
     * Helper that creates a Locator for the given address. The Locator is
     * created with the "URI" notation scheme.
     * 
     * @param adress
     *            the adress of the Locator to be created
     * @param factory
     *            a LocatorFactory that will be used to create the Locator.
     * @return the Locator created
     * @throws JellyTagException
     */
    public Locator createLocator(String adress, LocatorFactory factory)
            throws JellyTagException {

        try {
            return factory.createLocator("URI", adress);
        } catch (LocatorFactoryException e) {
            throw new JellyTagException(e);
        }
    }

    /**
     * Helper that creates a Locator for the given address. The Locator is
     * created with the "URI" notation scheme.
     * 
     * @param adress
     *            the adress of the Locator to be created
     * @param tmo
     *            any topicMapObject that is connected to a TopicMap.
     * @return the Locator created
     * @throws JellyTagException
     */
    public Locator createLocator(String adress, TopicMapObject tmo)
            throws JellyTagException {

        TopicMap tm;
        if (tmo instanceof TopicMap) {
            tm = (TopicMap) tmo;
        } else {
            tm = tmo.getTopicMap();
        }
        if (tm == null) {
            String msg = "Unable to create a Locator for adress: " + adress;
            msg += ". Reason: The given TopicMapObject is not attached to a TopicMap";
            throw new JellyTagException(msg);
        }
        return createLocator(adress, tm.getLocatorFactory());
    }

    /**
     * Merges the Topicmap from the given file into the basetm
     * 
     * @param basetm
     * @param file
     */
    public void mergeTopicMap(TopicMap basetm, File file)
            throws JellyTagException {
        TopicMapProvider provider = basetm.getProvider();

        try {
            TopicMapSource source = new SerializedTopicMapSource(file);
            provider.addTopicMap(source, basetm); // TODO
        } catch (TopicMapProviderException e) {
            throw new JellyTagException("Could not merge topic map from '"
                    + file.getAbsolutePath() + "': " + e);
        } catch (FileNotFoundException e) {
            throw new JellyTagException("Could not locate file to merge: "
                    + file.getAbsolutePath());
        } catch (MalformedURLException e) {
            throw new JellyTagException("Could not create URL from file: " + e);
        }
    }

    /**
     * Creates a Locator from the given adress and asserts that the Locator is
     * not already used as a SourceLocator in the given TopicMap.
     * 
     * @param tm
     * @param adress
     * @return the newly created Locaotr or null if adress was null.
     * @throws JellyTagException
     *             if either no Locator could be created from the given adress
     *             or if the given TopicMap already contains an Object with a
     *             SourceLocator-Adress that is equal to the given adress.
     */
    public Locator makeSourceLocator(TopicMap tm, String adress)
            throws JellyTagException {

        Locator sl = null;
        if (adress != null) {
            sl = createLocator(adress, tm.getLocatorFactory());

            if (tm.getObjectBySourceLocator(sl) != null) {
                // SourceLocator already points to an object
                // in the topicmap
                String msg = "The topicmap already contains a sourceLocator "
                        + "with the adress " + adress;
                throw new JellyTagException(msg);
            }
        }

        return sl;

    }

    private interface TMOCreator {

        TopicMapObject create(String id) throws Exception;
    }
}

