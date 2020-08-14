package it.unitn.wa.devisdm.lyricstrivia.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SongLyrics.class)
public abstract class SongLyrics_ {

	public static volatile SingularAttribute<SongLyrics, Integer> trackID;
	public static volatile SingularAttribute<SongLyrics, String> trackArtist;
	public static volatile SingularAttribute<SongLyrics, String> trackName;
	public static volatile SingularAttribute<SongLyrics, String> trackLyrics;

}

