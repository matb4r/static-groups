package example.staticgroups;

import peersim.core.CommonState;
import peersim.core.Network;
import peersim.core.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    public static int cycle = 0;

    public static HashMap<BigInteger, ArrayList<StaticGroupsProtocol>> GROUPS = new HashMap<>();

    public static ArrayList<StaticGroupsProtocol> NODES = new ArrayList<>();

    public static String generateIp(BigInteger groupNo, int m) {
        return "0.0." + groupNo + "." + new BigInteger(m, CommonState.r);
    }

    public static StaticGroupsProtocol getAnyCP(int pid) {
        Node n;
        do {
            n = Network.get(CommonState.r.nextInt(Network.size()));
        } while (n == null || n.isUp() == false);
        return ((StaticGroupsProtocol) n.getProtocol(pid));
    }

    public static StaticGroupsProtocol getRandomCP(StaticGroupsProtocol cp, int pid) {
        if (isOnlyOneGroupInNetwork(pid)) {
            return null;
        }
        Node n;
        do {
            n = Network.get(CommonState.r.nextInt(Network.size()));
        }
        while (n == null || n.isUp() == false || ((StaticGroupsProtocol) n.getProtocol(pid)).group.no.equals(cp.group.no));
        return ((StaticGroupsProtocol) n.getProtocol(pid));
    }

    public static boolean isOnlyOneGroupInNetwork(int pid) {

        BigInteger no = NODES.get(0).group.no;
        for (StaticGroupsProtocol cp : NODES) {
            if (!cp.group.no.equals(no)) {
                return false;
            }
        }

        return true;
    }

    public static StaticGroupsProtocol getFirstCPByNo(BigInteger no, int pid) {
        ArrayList<StaticGroupsProtocol> cps = GROUPS.get(no);
        if (cps.size() == 0) {
            return null;
        } else {
            return cps.get(0);
        }
    }

    public static void updateIps(BigInteger n, ArrayList<String> ips, int pid) {
        for (StaticGroupsProtocol cp : NODES) {
            if (cp.group.no.equals(n)) {
                cp.group.ips = ips;
            }
        }
    }

    public static void updateSuccessor(BigInteger n, Group successor, int pid) {
        for (StaticGroupsProtocol cp : NODES) {
            if (cp.group.no.equals(n)) {
                cp.successor = successor;
            }
        }
    }

    public static void updatePredecessor(BigInteger n, Group predecessor, int pid) {
        for (StaticGroupsProtocol cp : NODES) {
            if (cp.group.no.equals(n)) {
                cp.predecessor = predecessor;
            }
        }
    }

    public static void updateFingerTable(BigInteger n, Finger[] fingerTable, int pid) {
        for (StaticGroupsProtocol cp : NODES) {
            if (cp.group.no.equals(n)) {
                cp.fingerTable = fingerTable;
            }
        }
    }

    public static boolean inAB(BigInteger bid, BigInteger ba, BigInteger bb) {
        long id = bid.longValue();
        long a = ba.longValue();
        long b = bb.longValue();

        if (id == a || id == b)
            return true;
        if (id > a && id < b)
            return true;
        if (id < a && a > b && id < b)
            return true;
        if (id > b && a > b && id > a)
            return true;

        return false;
    }

    public static boolean betweenAB(BigInteger bid, BigInteger ba, BigInteger bb) {
        long id = bid.longValue();
        long a = ba.longValue();
        long b = bb.longValue();

        if (id == a || id == b)
            return false;
        if (id > a && id < b)
            return true;
        if (id < a && a > b && id < b)
            return true;
        if (id > b && a > b && id > a)
            return true;

        return false;
    }

    public static BigInteger generateUniqueNo(int idLength) {
        BigInteger no;
        do {
            no = new BigInteger(idLength, CommonState.r);
        } while (GROUPS.keySet().contains(no) && GROUPS.get(no).size() != 0);
        return no;
    }
}
