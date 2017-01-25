package ru.one.more.app.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by aboba on 23.01.17.
 */
@Entity
public class LogRecord {

    Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

}
