package net.sf.semantag.tm;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.net.LocatorFactoryException;
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
    protected static Topic createTopic(TopicMap tm, String id,
            String sourceLocator) throws JellyTagException {

        // Check the sourceLocator first.
        // Otherwise the topic would have been already
        // created, before we realize a potential
        // duplication of sourceLocators
        Locator sl = null;
        if (sourceLocator != null) {
            sl = createLocator(sourceLocator, tm.getLocatorFactory());

            if (tm.getObjectBySourceLocator(sl) != null) {
                // SourceLocator already points to an object
                // in the topicmaps
                String msg = "Illegal attempt to add a topic with an ";
                msg += "already existing sourceLocator (" + sourceLocator
                        + ").";
                throw new JellyTagException(msg);
            }
        }

        try {
            Topic t = tm.createTopic(id);
            if (sl != null) {
                t.addSourceLocator(sl);
            }
            return t;
        } catch (Exception e) {
            throw new JellyTagException("While creating Topic (ID: " + id
                    + "/SourceLocator: " + sourceLocator, e);
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
     * Merges the Topicmap from the given file into
     * the basetm
     * @param basetm
     * @param file
     */
    public static void mergeTopicMap(TopicMap basetm, File file) 
        throws JellyTagException
    {
        TopicMapProvider provider = basetm.getProvider();

        try {
          TopicMapSource source = new SerializedTopicMapSource(file);
          provider.addTopicMap(source, basetm); // TODO
        } catch (TopicMapProviderException e) {
          throw new JellyTagException("Could not merge topic map from '" +
                                     file.getAbsolutePath() + "': " + e);
        } catch (FileNotFoundException e) {
          throw new JellyTagException("Could not locate file to merge: " +
                                     file.getAbsolutePath());
        } catch (MalformedURLException e) {
          throw new JellyTagException("Could not create URL from file: " + e);
        }
      }

}