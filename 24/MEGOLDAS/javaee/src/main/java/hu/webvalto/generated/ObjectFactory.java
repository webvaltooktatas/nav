
package hu.webvalto.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hu.webvalto.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FelhasznaloLetrehozasa_QNAME = new QName("http://ws.backend.webvalto.hu/", "felhasznaloLetrehozasa");
    private final static QName _FelhasznaloLetrehozasaResponse_QNAME = new QName("http://ws.backend.webvalto.hu/", "felhasznaloLetrehozasaResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hu.webvalto.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FelhasznaloLetrehozasa }
     * 
     */
    public FelhasznaloLetrehozasa createFelhasznaloLetrehozasa() {
        return new FelhasznaloLetrehozasa();
    }

    /**
     * Create an instance of {@link FelhasznaloLetrehozasaResponse }
     * 
     */
    public FelhasznaloLetrehozasaResponse createFelhasznaloLetrehozasaResponse() {
        return new FelhasznaloLetrehozasaResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FelhasznaloLetrehozasa }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.backend.webvalto.hu/", name = "felhasznaloLetrehozasa")
    public JAXBElement<FelhasznaloLetrehozasa> createFelhasznaloLetrehozasa(FelhasznaloLetrehozasa value) {
        return new JAXBElement<FelhasznaloLetrehozasa>(_FelhasznaloLetrehozasa_QNAME, FelhasznaloLetrehozasa.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FelhasznaloLetrehozasaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.backend.webvalto.hu/", name = "felhasznaloLetrehozasaResponse")
    public JAXBElement<FelhasznaloLetrehozasaResponse> createFelhasznaloLetrehozasaResponse(FelhasznaloLetrehozasaResponse value) {
        return new JAXBElement<FelhasznaloLetrehozasaResponse>(_FelhasznaloLetrehozasaResponse_QNAME, FelhasznaloLetrehozasaResponse.class, null, value);
    }

}
