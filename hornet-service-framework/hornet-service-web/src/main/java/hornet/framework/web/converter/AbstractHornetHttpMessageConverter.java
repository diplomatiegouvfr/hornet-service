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

import hornet.framework.export.ExportModelService;

import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import hornet.framework.export.ExportModelService;

/**
 * @author MEAE - Ministère de l'Europe et des Affaires étrangères
 * 
 *        Types génériques :
 *        <ul>
 *        <li>T : Type de l'objet à convertir</li>
 *        <li>U : Type du converter (Etend obligatoirement de {@link ExportModelService}</li>
 *        </ul>
 *        Ce Converter abstrait les couches :
 *        <ul>
 *        <li>Récupération du {@link ExportModelService} gérant la classe à exporter</li>
 *        <li>Appel au service pour écrire dans le flux et fermeture du flux</li>
 *        </ul>
 */
public abstract class AbstractHornetHttpMessageConverter<T, U extends ExportModelService<T>> extends
            AbstractHttpMessageConverter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractHornetHttpMessageConverter.class);

    /**
     * @param mediaType
     *            : le mediaType supporté par le converter
     */
    public AbstractHornetHttpMessageConverter(
                final MediaType mediaType) {

        super(mediaType);
    }

    /**
     * Renvoie la liste des services gérant le type d'export demandé
     *
     */
    protected abstract U[] getServicesExport();

    /**
     * Retourne le service gérant l'export de l'objet donné en parametre
     *
     * @param toExport
     * @return
     * @throws HttpMessageNotWritableException
     */
    protected U getServiceExport(final Object toExport) throws HttpMessageNotWritableException {

        U theService = null;

        for (final U exportServiceTest : getServicesExport()) {
            if (exportServiceTest.supports(toExport.getClass())) {
                theService = exportServiceTest;
                break;
            }
        }
        if (theService == null) {
            throw new HttpMessageNotWritableException("Aucun service ne sait gérer l'export de l'objet "
                        + toExport.getClass());
        }
        return theService;
    }

    /**
     * Parcourt la liste des services d'export référencés. Si un des services sait gérer l'export demandé,
     * alors retourne true
     */
    @Override
    protected boolean supports(final Class<?> clazz) {

        for (final ExportModelService<T> exportService : getServicesExport()) {
            if (exportService.supports(clazz)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Le read n'est pas implémenté (impossible de convertir un PDF, XLS, .. vers un bean Java)
     */
    @Override
    public boolean canRead(final Class<?> clazz, final MediaType mediaType) {

        return false;
    }

    /**
     * Le read n'est pas implémenté (impossible de convertir un PDF, XLS, .. vers un bean Java)
     */
    @Override
    protected T readInternal(final Class<? extends T> clazz, final HttpInputMessage inputMessage)
        throws IOException, HttpMessageNotReadableException {

        throw new HttpMessageNotReadableException(
                    "Import Non implémenté : ce MessageConverter ne gère que l'export", inputMessage);
    }

    /**
     * Délègue l'écriture du flux de sortie à partir du bean à exporter
     *
     * @param toExport
     * @param os
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    protected abstract void ecrireFichier(final T toExport, final OutputStream os) throws IOException,
        HttpMessageNotWritableException;

    /**
     * Encapsule l'appel à ecrireFichier(). Ajoute la logique d'ouverture / fermeture des flux
     */

    @Override
    protected void writeInternal(final T toExport, final HttpOutputMessage outputMessage) throws IOException,
        HttpMessageNotWritableException {

        LOG.debug("Demande d'export de {}", toExport.getClass());

        try (OutputStream os = outputMessage.getBody()) {
            ecrireFichier(toExport, os);
        } catch (final Exception e) {
            LOG.error("Erreur lors de la demande d'export de la classe {} : {}", toExport.getClass(),
                e.getMessage(), e);
            throw e;
        }
    }

}
