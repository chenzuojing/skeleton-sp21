package gitlet;

import java.io.IOException;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Zuojing Chen
 */
public class Main {

    private static Repository repo = new Repository();

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }

        String firstArg = args[0];


        switch (firstArg) {
            case "init":
                validateNumArgs("init", args, 1);
                repo.init();
                break;
            case "add":
                checkInitialized();
                validateNumArgs("add", args, 2);
                repo.add(args[1]);
                break;
            case "commit":
                checkInitialized();
                validateNumArgs("commit", args, 2);
                repo.commit(args[1]);
                break;
            case "log":
                checkInitialized();
                validateNumArgs("log", args, 1);
                repo.log();
                break;
            case "checkout":
                checkInitialized();
                validateCheckout(args);
                break;
            default:
                exitWithError("No command with that name exists.");
        }
    }


    /**
     * Checks the number of arguments versus the expected number, throws a RuntimeException if they do
     * not match.
     *
     * @param cmd  Name of command you are validating
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            exitWithError("Incorrect operands.");
        }
    }

    public static void checkInitialized() {
        if (!Repository.GITLET_DIR.exists())
            exitWithError("Not in an initialized Gitlet directory.");
    }

    private static void validateCheckout(String[] args) throws IOException {
//        if (args.length == 2) {
//            if (!Branch.hasBranch(args[1])) {
//                exitWithError("No such branch exists.");
//            }
//            repo.checkoutBranch(args[1]);
//        }

        if (args.length == 3) {
            if (!args[1].equals("--")) {
                exitWithError("Incorrect operands.");
            }
            repo.checkoutFile(args[2]);
        }

        if (args.length == 4) {
            if (!args[2].equals("--")) {
                exitWithError("Incorrect operands.");
            }
            if (!repo.containsCommitId(args[1])) {
                exitWithError("No commit with that id exists.");
            }
            repo.checkoutCommit(args[1], args[3]);
        }
    }


    public static void exitWithError(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
