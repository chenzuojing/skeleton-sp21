package gitlet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Staging implements Serializable {

    private Map<String, String> stagedForAddition;
    Set<String> stagedForRemoval;

    public Staging() {
        this.stagedForAddition = new TreeMap<>();
        this.stagedForRemoval = new HashSet<>();
    }

    public Staging(Map<String, String> trackedFiles, Set<String> untrackedFiles) {
        this.stagedForAddition = trackedFiles;
        this.stagedForRemoval = untrackedFiles;
    }

    public Map<String, String> getStagedForAddition() {
        return stagedForAddition;
    }

    public Set<String> getStagedForRemoval() {
        return stagedForRemoval;
    }

    public void addToAddition(String fileName, String blobSha) {
        if (stagedForRemoval.contains(fileName))
            stagedForRemoval.remove(fileName);
        stagedForAddition.put(fileName, blobSha);
    }

    public void removeFromAddition(String fileName) {
        stagedForAddition.remove(fileName);
    }

    public void addToRemoval(String fileName) {
        stagedForRemoval.add(fileName);
    }

    public boolean containsForAddition(String fileName) {
        return stagedForAddition.containsKey(fileName);
    }

    public boolean containsForRemoval(String fileName) {
        return stagedForRemoval.contains(fileName);
    }

    public boolean hasUncommitedChange() {
        return !stagedForAddition.isEmpty() || !stagedForRemoval.isEmpty();
    }


    public void save() {
        Utils.writeObject(Repository.STAGE_File, this);
    }

    public static Staging load() {
        return Utils.readObject(Repository.STAGE_File, Staging.class);
    }

}
