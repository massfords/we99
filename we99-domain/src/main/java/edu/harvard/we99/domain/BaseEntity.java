package edu.harvard.we99.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Base class for our entities. Not much value here beyond configuring some base
 * serialization params.
 *
 * todo -- should move this behavior to the Jackson marshaller/unmarshaller if possible
 *
 * @author mford
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseEntity {
}
