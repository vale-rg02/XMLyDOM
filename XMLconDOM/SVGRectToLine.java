import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import javax.xml.transform.TransformerFactory;

public class SVGRectToLine {
    
    public static void main(String[] args) {
        try {
            // 1. Leer el archivo SVG con el API DOM
            File inputFile = new File("archivo_original.svg");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            // 2. Obtener todos los elementos <rect>
            NodeList rectList = doc.getElementsByTagName("rect");

            // Recorrer la lista de <rect> y reemplazarlos por <line>
            for (int i = 0; i < rectList.getLength(); i++) {
                Node rectNode = rectList.item(i);
                
                if (rectNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element rectElement = (Element) rectNode;

                    // Obtener los atributos del <rect> (x, y, width, height)
                    float x = Float.parseFloat(rectElement.getAttribute("x"));
                    float y = Float.parseFloat(rectElement.getAttribute("y"));
                    float width = Float.parseFloat(rectElement.getAttribute("width"));
                    float height = Float.parseFloat(rectElement.getAttribute("height"));

                    // Crear las cuatro líneas que forman el rectángulo
                    Element line1 = doc.createElement("line");
                    line1.setAttribute("x1", String.valueOf(x));
                    line1.setAttribute("y1", String.valueOf(y));
                    line1.setAttribute("x2", String.valueOf(x + width));
                    line1.setAttribute("y2", String.valueOf(y));
                    line1.setAttribute("stroke", rectElement.getAttribute("stroke"));
                    line1.setAttribute("stroke-width", rectElement.getAttribute("stroke-width"));

                    Element line2 = doc.createElement("line");
                    line2.setAttribute("x1", String.valueOf(x + width));
                    line2.setAttribute("y1", String.valueOf(y));
                    line2.setAttribute("x2", String.valueOf(x + width));
                    line2.setAttribute("y2", String.valueOf(y + height));
                    line2.setAttribute("stroke", rectElement.getAttribute("stroke"));
                    line2.setAttribute("stroke-width", rectElement.getAttribute("stroke-width"));

                    Element line3 = doc.createElement("line");
                    line3.setAttribute("x1", String.valueOf(x + width));
                    line3.setAttribute("y1", String.valueOf(y + height));
                    line3.setAttribute("x2", String.valueOf(x));
                    line3.setAttribute("y2", String.valueOf(y + height));
                    line3.setAttribute("stroke", rectElement.getAttribute("stroke"));
                    line3.setAttribute("stroke-width", rectElement.getAttribute("stroke-width"));

                    Element line4 = doc.createElement("line");
                    line4.setAttribute("x1", String.valueOf(x));
                    line4.setAttribute("y1", String.valueOf(y + height));
                    line4.setAttribute("x2", String.valueOf(x));
                    line4.setAttribute("y2", String.valueOf(y));
                    line4.setAttribute("stroke", rectElement.getAttribute("stroke"));
                    line4.setAttribute("stroke-width", rectElement.getAttribute("stroke-width"));

                    // Reemplazar el <rect> por las cuatro líneas
                    Node parent = rectElement.getParentNode();
                    parent.removeChild(rectElement);  // Eliminar el <rect>

                    // Agregar las cuatro líneas en su lugar
                    parent.appendChild(line1);
                    parent.appendChild(line2);
                    parent.appendChild(line3);
                    parent.appendChild(line4);
                }
            }

            // 3. Guardar el archivo modificado como una copia
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("archivo_modificado.svg"));
            transformer.transform(source, result);

            System.out.println("El archivo SVG ha sido modificado y guardado como archivo_modificado.svg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
