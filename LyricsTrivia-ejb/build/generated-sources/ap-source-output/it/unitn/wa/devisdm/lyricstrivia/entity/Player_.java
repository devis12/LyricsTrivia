package it.unitn.wa.devisdm.lyricstrivia.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Player.class)
public abstract class Player_ {

	public static volatile SingularAttribute<Player, byte[]> salt;
	public static volatile SingularAttribute<Player, Date> birthdate;
	public static volatile SingularAttribute<Player, Character> gender;
	public static volatile SingularAttribute<Player, Integer> won;
	public static volatile SingularAttribute<Player, byte[]> pwd;
	public static volatile SingularAttribute<Player, Integer> played;
	public static volatile SingularAttribute<Player, Boolean> confirmed;
	public static volatile SingularAttribute<Player, String> email;
	public static volatile SingularAttribute<Player, String> username;

}

