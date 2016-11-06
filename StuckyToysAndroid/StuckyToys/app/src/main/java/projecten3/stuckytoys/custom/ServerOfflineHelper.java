package projecten3.stuckytoys.custom;


//simple helper class; setting SERVEROFFLINE to true makes it so no db calls are issued and default users/members/... are created for testing
public interface ServerOfflineHelper {
    public static boolean SERVEROFFLINE = false;
}
