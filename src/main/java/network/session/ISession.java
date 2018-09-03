package network.session;

public interface ISession {
    int getId();

    void write(Object obj);
}
