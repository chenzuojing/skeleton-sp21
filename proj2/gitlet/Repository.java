package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.join;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 * TODO: It's a good idea to give a description here of what else this Class
 * does at a high level.
 *
 * @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File COMMITS_DIR = join(GITLET_DIR, "commits");
    public static final File STAGE_File = join(GITLET_DIR, "STAGE");
    public static final File HEAD_FILE = join(GITLET_DIR, "HEAD.txt");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    static Staging stagingArea = new Staging();


    public void init() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        setupPersistence();
        Commit initCommit = new Commit();
        String sha1 = Commit.getCommitSha(initCommit);
        Head.setGlobalHead(sha1);
        Head.setBranchHead("master", sha1);
        initCommit.save();
        stagingArea.save();
    }


    private static void setupPersistence() throws IOException {
        GITLET_DIR.mkdir();
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        STAGE_File.createNewFile();
        HEAD_FILE.createNewFile();
        REFS_DIR.mkdir();
    }


    public void add(String fileName) throws IOException {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }

        Blob blob = new Blob(fileName);
        blob.save();
        String blobSha = blob.getBlobSha();
        Commit head = Commit.load(Head.getGlobalHead());
        stagingArea = Staging.load();

        if (Repository.trackedByHeadCommit(fileName)) {
            if (blobSha.equals(head.getTrackedFiles().get(fileName))) {
                stagingArea.removeFromAddition(fileName);
                stagingArea.save();
                return;
            }
        }

        stagingArea.addToAddition(fileName, blobSha);
        stagingArea.save();
    }


    public void commit(String message) throws IOException {
        stagingArea = Staging.load();

        if (!stagingArea.hasUncommitedChange()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (message.length() == 0) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        Commit head = Commit.load(Head.getGlobalHead());
        Commit newHead = new Commit(message, new Date());
        newHead.setParent(Head.getGlobalHead());

        Map<String, String> newTrackedFiles = new HashMap<>(head.getTrackedFiles());

        for (String updateFile : stagingArea.getStagedForAddition().keySet()) {
            newTrackedFiles.put(updateFile, stagingArea.getStagedForAddition().get(updateFile));
        }
        for (String removedFile : stagingArea.getStagedForRemoval()) {
            newTrackedFiles.remove(removedFile);
        }

        newHead.setTrackedFiles(newTrackedFiles);
        Head.setGlobalHead(Commit.getCommitSha(newHead));
        newHead.save();

        stagingArea = new Staging();
        stagingArea.save();
    }

    public void log() {
        String headSha = Head.getGlobalHead();
        Commit head = Commit.load(headSha);
        while (!head.getMessage().equals("initial commit")) {
            System.out.println("===");
            System.out.println("commit " + headSha);
            System.out.println("Date: " + head.getTimeString());
            System.out.println(head.getMessage());
            System.out.println();
            headSha = head.getParent();
            head = Commit.load(headSha);
        }
        System.out.println("===");
        System.out.println("commit " + headSha);
        System.out.println("Date: " + head.getTimeString());
        System.out.println(head.getMessage());
        System.out.println();
    }


    public void checkoutFile(String fileName) throws IOException {
        String headSha = Head.getGlobalHead();
        Commit head = Commit.load(headSha);
        Map<String, String> trackedFiles = head.getTrackedFiles();

        if (!trackedFiles.containsKey(fileName)) {
            Main.exitWithError("File does not exist in that commit.");
        } else {
            String blobSha = trackedFiles.get(fileName);
            File blobFile = join(BLOBS_DIR, blobSha);
            Blob blob = Blob.load(blobFile);
            restoreFileInCWD(blob);
        }
    }


    public void checkoutCommit(String commitId, String fileName) throws IOException {
        Commit commit = Commit.load(commitId);
        Map<String, String> trackedFiles = commit.getTrackedFiles();

        if (!trackedFiles.containsKey(fileName)) {
            Main.exitWithError("File does not exist in that commit.");
        } else {
            String blobSha = trackedFiles.get(fileName);
            File blobFile = join(BLOBS_DIR, blobSha);
            Blob blob = Blob.load(blobFile);
            restoreFileInCWD(blob);
        }
    }


    public void restoreFileInCWD(Blob blob) throws IOException {
        //String CWD = System.getProperty("user.dir");
        File file = join(CWD, blob.getFileName());
        file.createNewFile();
        Utils.writeContents(file, blob.getFileContent());
    }

    public static boolean trackedByHeadCommit(String fileName) {
        Commit head = Commit.load(Head.getGlobalHead());
        return head.getTrackedFiles().containsKey(fileName);
    }

    public boolean containsCommitId(String targetCommitId) {
        for (String sha : COMMITS_DIR.list()) {
            if (findMatchId(sha, targetCommitId))
                return true;
        }
        return false;
    }

    public boolean findMatchId(String commitSHA1, String commitId) {
        return commitSHA1.substring(0, commitId.length()).equals(commitId);
    }

}
