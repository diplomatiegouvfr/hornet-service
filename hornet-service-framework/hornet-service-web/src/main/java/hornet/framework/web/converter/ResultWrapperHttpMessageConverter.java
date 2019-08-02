/**
 * Copyright ou © ou Copr. Ministère de l'Europe et des Affaires étrangères (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * Ce logiciel est un programme informatique servant à faciliter la création
 * d'applications Web conformément aux référentiels généraux français : RGI, RGS et RGAA
 * <p/>
 * Ce logiciel est régi par la licence CeCILL soumise au droit français et
 * respectant les principes de diffusion des logiciels libres. Vous pouvez
 * utiliser, modifier et/ou redistribuer ce programme sous les conditions
 * de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA
 * sur le site "http://www.cecill.info".
 * <p/>
 * En contrepartie de l'accessibilité au code source et des droits de copie,
 * de modification et de redistribution accordés par cette licence, il n'est
 * offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
 * seule une responsabilité restreinte pèse sur l'auteur du programme,  le
 * titulaire des droits patrimoniaux et les concédants successifs.
 * <p/>
 * A cet égard  l'attention de l'utilisateur est attirée sur les risques
 * associés au chargement,  à l'utilisation,  à la modification et/ou au
 * développement et à la reproduction du logiciel par l'utilisateur étant
 * donné sa spécificité de logiciel libre, qui peut le rendre complexe à
 * manipuler et qui le réserve donc à des développeurs et des professionnels
 * avertis possédant  des  connaissances  informatiques approfondies.  Les
 * utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
 * logiciel à leurs besoins dans des conditions permettant d'assurer la
 * sécurité de leurs systèmes et ou de leurs données et, plus généralement,
 * à l'utiliser et l'exploiter dans les mêmes conditions de sécurité.
 * <p/>
 * Le fait que vous puissiez accéder à cet en-tête signifie que vous avez
 * pris connaissance de la licence CeCILL, et que vous en avez accepté les
 * termes.
 * <p/>
 * <p/>
 * Copyright or © or Copr. Ministry for Europe and Foreign Affairs (2017)
 * <p/>
 * pole-architecture.dga-dsi-psi@diplomatie.gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to facilitate creation of
 * web application in accordance with french general repositories : RGI, RGS and RGAA.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 *
 */
package hornet.framework.web.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import hornet.framework.web.bo.Result;
import hornet.framework.web.controller.NullOrVoidJsonResponseBodyAdvice;

/**
 * @author buganp
 *
 */
public class ResultWrapperHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    final static class VoidSerializer extends StdSerializer<Void> {
        public final static VoidSerializer instance = new VoidSerializer();

        private VoidSerializer() {
            super(Void.TYPE);
        }

        @Override
        public void serialize(final Void value, final JsonGenerator jgen,
                    final SerializerProvider provider) throws IOException,
        JsonGenerationException {
            jgen.writeNull();
        }

        @Override
        public JsonNode getSchema(final SerializerProvider provider, final Type typeHint)
                    throws JsonMappingException {
            return createSchemaNode("null");
        }

        @Override
        public void acceptJsonFormatVisitor(final JsonFormatVisitorWrapper visitor,
                    final JavaType typeHint) throws JsonMappingException {
            visitor.expectNullFormat(typeHint);
        }
    }

    public ResultWrapperHttpMessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().serializers(VoidSerializer.instance).build());
    }

    /* (non-Javadoc)
     * @see org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter#writeInternal(java.lang.Object, org.springframework.http.HttpOutputMessage)
     */
    @Override
    protected void writeInternal(final Object object, final HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        if (object instanceof Result) {
            super.writeInternal(object, outputMessage);
        } else {
            final HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            final Result result = new Result(object == NullOrVoidJsonResponseBodyAdvice.NULL_OBJECT ? null : object);
            result.setUrl(req.getRequestURI());

            result.setStatus(200);

            super.writeInternal(result, outputMessage);
        }
    }

    @Override
    protected void writeInternal(final Object object, final Type type, final HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {

        if (object instanceof Result) {
            super.writeInternal(object, type, outputMessage);
        } else {

            final HttpServletRequest req =
                        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
            final Result result =
                        new Result(object == NullOrVoidJsonResponseBodyAdvice.NULL_OBJECT ? null : object);
            result.setUrl(req.getRequestURI());

            result.setStatus(200);

            super.writeInternal(result, Result.class, outputMessage);

        }
    }
}
