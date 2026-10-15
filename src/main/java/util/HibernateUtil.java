package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/** Une seule factory, partagée par tous les DAO jusqu'à la fermeture du programme. */
public final class HibernateUtil {
  private HibernateUtil() {}

  private static class Holder {
    private static final SessionFactory FACTORY = build();
  }

  private static SessionFactory build() {
    Configuration c = new Configuration().configure();
    override(c, "DB_URL", "hibernate.connection.url");
    override(c, "DB_USER", "hibernate.connection.username");
    override(c, "DB_PASSWORD", "hibernate.connection.password");
    c.setProperty(
        "hibernate.show_sql",
        System.getProperty("hibernate.show_sql", c.getProperty("hibernate.show_sql")));
    return c.buildSessionFactory();
  }

  private static void override(Configuration c, String variable, String key) {
    String value = System.getenv(variable);
    if (value != null) c.setProperty(key, value);
  }

  public static SessionFactory getSessionFactory() {
    return Holder.FACTORY;
  }

  public static void shutdown() {
    getSessionFactory().close();
    com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
  }
}
