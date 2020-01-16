import hu.webvalto.service.BankAPIRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {

        Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        jndiProps.put("jboss.naming.client.ejb.context", true);

        // create a context passing these properties
        Context ctx = new InitialContext(jndiProps);
        // lookup
        BankAPIRemote bankRemote = (BankAPIRemote)ctx.lookup("javaee/Bank!hu.webvalto.service.BankAPIRemote");


        bankRemote.szamlanyitas("attila");

        ctx.close();
    }
}
