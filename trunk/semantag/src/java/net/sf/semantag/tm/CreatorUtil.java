package net.sf.semantag.tm;

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
 * Utility class that assembles methods to manage TopicMapObjects
 * 
 * 
 * @author cf
 * @version 0.1, created on 07.09.2004
 */
public class CreatorUtil {

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
    protected static Topic createTopic(final TopicMap tm, String id,
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
    protected static Association createAssociation(final TopicMap tm, String id,
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
    protected static Occurrence createOccurrence(final Topic t, String id,
            String sourceLocator) throws JellyTagException {

        // define a creator that creates an Occurrence
        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                return t.createOccurrence(id);
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
    public static BaseName createBasename(final Topic t, String id,
            String sourceLocator) throws JellyTagException {
        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                return t.createName(id);
            }
        };

        return (BaseName) createTMO(t.getTopicMap(), c, id, sourceLocator);
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
    public static Member createMember(final Association a, String id,
            String sourceLocator) throws JellyTagException {

        TMOCreator c = new TMOCreator() {
            public TopicMapObject create(String id) throws Exception {
                return a.createMember(id);
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
    public static TopicMapObject createTMO(TopicMap tm, TMOCreator tmoCreator,
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
                    + id + "/SourceLocator: " + sourceLocator+")", e);
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
    public static Locator createLocator(String adress, LocatorFactory factory)
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
    public static Locator createLocator(String adress, TopicMapObject tmo)
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
    public static void mergeTopicMap(TopicMap basetm, File file)
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
    public static Locator makeSourceLocator(TopicMap tm, String adress)
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

