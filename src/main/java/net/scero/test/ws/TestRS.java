package net.scero.test.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement(namespace = "http://net.scero.test.ws")
@XmlType(namespace = "http://net.scero.test.ws")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class TestRS implements Serializable{
    private static final long serialVersionUID = -762403195304378867L;

    @XmlElement(required = true)
    private int status;
    @XmlElement(required = false)
    private String message;
}
