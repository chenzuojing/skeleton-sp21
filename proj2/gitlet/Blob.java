package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Blob implements Serializable {

    private String fileName;

    private byte[] fileContent;

    private String blobSha;

    public Blob(String fileName) {
        this.fileName = fileName;
        this.fileContent = Utils.readContents(Utils.join(Repository.CWD, fileName));
        this.blobSha = Utils.sha1(fileContent);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBlobSha() {
        return blobSha;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    /**
     * Save a blob as a byte array. Use SHA1 as its file name.
     */
    public void save() throws IOException {
        File blobFile = Utils.join(Repository.BLOBS_DIR, this.getBlobSha());
        blobFile.createNewFile();
        Utils.writeObject(blobFile, this);
    }

    /**
     * Return a blob object from the byte array.
     *
     * @param blobFile the blob
     */
    public static Blob load(File blobFile) {
        return Utils.readObject(blobFile, Blob.class);
    }

}
