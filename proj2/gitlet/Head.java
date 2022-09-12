package gitlet;

import java.io.File;
import java.io.IOException;

public class Head {


    public static void setGlobalHead(String globalHeadSha) {
        Utils.writeContents(Repository.HEAD_FILE, globalHeadSha);
    }

    public static void setBranchHead(String branchName, String headSha) throws IOException {
        File file = Utils.join(Repository.REFS_DIR, branchName);
        file.createNewFile();
        Utils.writeContents(file, headSha);
    }

    public static String getGlobalHead() {
        return Utils.readContentsAsString(Repository.HEAD_FILE);
    }

}
