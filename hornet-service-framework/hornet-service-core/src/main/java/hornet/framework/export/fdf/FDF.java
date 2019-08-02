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
package hornet.framework.export.fdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import hornet.framework.export.fdf.exception.FDFExportException;

/**
 *
 * Projet hornetserver. Utility class to merge FDF with data to produce a PDF
 *
 * @date 29 déc. 2009
 *
 */
public final class FDF {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(FDF.class);

    /**
     * The Enum FieldBoxPositions.
     */
    enum FieldBoxPositions {

        /** The page. */
        PAGE,
        /** The llx. */
        LLX,
        /** The lly. */
        LLY,
        /** The urx. */
        URX,
        /** The ury. */
        URY
    };

    /**
     * Private constructor for utility class.
     */
    private FDF() {

        // nothing.
    }

    /**
     * Thread-safe utility method for PDF generation, based on a FDF template stream and a data object to
     * merge. Fields defined in FDF that can't be merged (not corresponding field in data object) are stored
     * in FDF Result.
     *
     * @param in
     *            input stream of PDF/FDF (template)
     * @param out
     *            output stream of final PDF
     * @param data
     *            a javabean or a java.util.Map. Nested properties are allowed this field names access pattern
     *            : "propA:propB:propC".
     * @return FDF result object (not null)
     */
    public static FDFRes export(final InputStream in, final OutputStream out, final Object data) {

        PdfReader pdfIn = null;
        PdfStamper stamper = null;
        final FDFRes res = new FDFRes();

        try {
            pdfIn = new PdfReader(in);
            stamper = new PdfStamper(pdfIn, out);
            final AcroFields form = stamper.getAcroFields();

            // fusion champs FDF avec le bean
            for (final Object nomField : form.getFields().keySet()) {
                fusionChamp(data, stamper, res, form, nomField);
            }

            // la ligne suivante supprime le formulaire
            stamper.setFormFlattening(true);

        } catch (final IOException ex) {
            throw new FDFExportException(ex);
        } catch (final DocumentException ex) {
            throw new FDFExportException(ex);
        } finally {
            try {
                if (stamper != null) {
                    stamper.close();
                }
            } catch (final Exception ex) {
                LOG.error("Erreur", ex);
            }
        }

        return res;
    }

    /**
     * Fusion d'un champ FDF.
     *
     * @param data
     *            the data
     * @param stamper
     *            the stamper
     * @param res
     *            the res
     * @param form
     *            the form
     * @param nomField
     *            the nom field
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws DocumentException
     *             the document exception
     */
    private static void fusionChamp(final Object data, final PdfStamper stamper, final FDFRes res,
                final AcroFields form, final Object nomField) throws IOException, DocumentException {

        // utilisation du ":" comme séparateur d'accès.
        // le "." étant remplacé par "_" par openoffice lors
        // de la conversion PDF.
        final String nomFieldStr = nomField.toString().replace(':', '.');

        Object value = null;
        try {
            value = PropertyUtils.getProperty(data, nomFieldStr);
        } catch (final Exception ex) {
            res.getUnmerged().add(nomFieldStr);
        }

        String valueStr;

        if (value == null) {
            valueStr = ""; // itext n'accepte pas les valeurs
            // nulles
            form.setField(nomField.toString(), valueStr);
        } else if (value instanceof FDFImage) {
            final FDFImage imValue = (FDFImage) value;
            final float[] positions = form.getFieldPositions(nomField.toString());
            final PdfContentByte content = stamper.getOverContent(1);
            final Image im = Image.getInstance(imValue.getData());
            if (imValue.isFit()) {
                content.addImage(im, positions[FieldBoxPositions.URX.ordinal()]
                            - positions[FieldBoxPositions.LLX.ordinal()], 0, 0,
                            positions[FieldBoxPositions.URY.ordinal()] - positions[FieldBoxPositions.LLY.ordinal()],
                            positions[FieldBoxPositions.LLX.ordinal()], positions[FieldBoxPositions.LLY.ordinal()]);
            } else {
                content.addImage(im, im.getWidth(), 0, 0, im.getHeight(), positions[1], positions[2]);
            }
        } else if (value instanceof Date) {
            // format par défaut date
            valueStr = DateFormat.getDateInstance(DateFormat.SHORT).format(value);
            form.setField(nomField.toString(), valueStr);
        } else if (value instanceof Boolean) {
            // format par spécial pour Checkbox
            if (Boolean.TRUE.equals(value)) {
                valueStr = "Yes";
            } else {
                valueStr = "No";
            }
            form.setField(nomField.toString(), valueStr);
        } else {
            // format par défaut
            valueStr = value.toString();
            form.setField(nomField.toString(), valueStr);
        }
    }
}
