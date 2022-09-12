package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a gitlet commit object.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    private String message;

    /**
     * The time of this Commit.
     */
    private Date timeStamp;

    /**
     * The parents of this Commit.
     */
    private String[] parents = new String[2];

    /**
     * The files and their versions(sha1) of this Commit.
     */
    private Map<String, String> trackedFiles;

    /* TODO: fill in the rest of this class. */

    /**
     * Creates a init Commit object with the specified parameters.
     */
    public Commit() {
        this.message = "initial commit";
        this.timeStamp = new Date(0);
        trackedFiles = new TreeMap<>();
    }

    public Commit(String message, Date timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
        trackedFiles = new TreeMap<>();
    }

    public String getMessage() {
        return message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getTimeString() {
        Format formatter = new SimpleDateFormat("EEE LLL d HH:mm:ss y Z");
        return formatter.format(timeStamp);
    }

    public String getParent() {
        return parents[0];
    }

    public String getSecParent() {
        return parents[1];
    }

    public Map<String, String> getTrackedFiles() {
        return trackedFiles;
    }

    public void setParent(String parent) {
        parents[0] = parent;
    }

    public void setSecParent(String secParent) {
        parents[1] = secParent;
    }

    public void setTrackedFiles(Map<String, String> trackedFiles) {
        this.trackedFiles = trackedFiles;
    }

    /**
     * Saves a Commit to a file for future use.
     */
    public void save() throws IOException {
        String sha1 = getCommitSha(this);
        File commitFile = Utils.join(Repository.COMMITS_DIR, sha1);
        commitFile.createNewFile();
        Utils.writeObject(commitFile, this);
    }


    public static String getCommitSha(Commit commit) {
        return Utils.sha1((Object) Utils.serialize(commit));
    }

    /**
     * Reads in and deserializes a commit from a file with name sha1 in commits folder.
     *
     * @param sha1 Name of commit to load
     * @return Commit read from file
     */
    public static Commit load(String sha1) {
        File commitFile = Utils.join(Repository.COMMITS_DIR, sha1);
        return Utils.readObject(commitFile, Commit.class);
    }
}
