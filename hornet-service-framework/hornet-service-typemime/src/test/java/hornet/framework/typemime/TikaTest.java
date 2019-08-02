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
package hornet.framework.typemime;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hornet.framework.typemime.bo.TypeMime;
import hornet.framework.typemime.parseur.TikaParseur;
import hornet.framework.typemime.utility.ConstantesTypeMime;

public class TikaTest {

    /** logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TikkaTest.class);

    /**
     * <code>ressources</code> the ressources
     */
    private static String ressources = "hornet/framework/typemime/ressources";

    /**
     * <code>gifFile</code> the gifFile
     */
    private static String gifFile = "test.gif";

    /**
     * <code>gifAnimeFile</code> the gifAnimeFile
     */
    private static String gifAnimeFile = "test_anime.gif";

    /**
     * <code>pngFile</code> the pngFile
     */
    private static String pngFile = "test.png";

    /**
     * <code>jpgFile</code> the jpgFile
     */
    private static String jpgFile = "test.jpg";

    /**
     * <code>textFile</code> the textFile
     */
    private static String textFile = "test.txt";

    /**
     * <code>rtfFile</code> the rtfFile
     */
    private static String rtfFile = "test.rtf";

    /**
     * <code>pdfFile</code> the pdfFile
     */
    private static String pdfFile = "test.pdf";

    /**
     * <code>excel2KFile</code> the excel2KFile
     */
    private static String excel2KFile = "test_excel_2000.xls";

    /**
     * <code>excel2007File</code> the excel2007File
     */
    private static String excel2007File = "test_excel_2007.xlsx";

    /**
     * <code>word95File</code> the word95File
     */
    private static String word95File = "test_word_6.0_95.doc";

    /**
     * <code>word2KFile</code> the word2KFile
     */
    private static String word2KFile = "test_word_2000.doc";

    /**
     * <code>word2007File</code> the word2007File
     */
    private static String word2007File = "test_word_2007.docx";

    /**
     * <code>openOfficeSheet</code> the openOfficeSheet
     */
    private static String openOfficeSheet = "test_open_office.ods";

    /**
     * <code>openOfficeText</code> the openOfficeText
     */
    private static String openOfficeText = "test_open_office.odt";

    /**
     * <code>archiveTar</code> the archiveTar
     */
    private static String archiveTar = "test.tar";

    /**
     * <code>archiveTarGz</code> the archiveTarGz
     */
    private static String archiveTarGz = "test.tar.gz";

    /**
     * <code>archiveGz</code> the archiveGz
     */
    private static String archiveGz = "test.txt.gz";

    /**
     * <code>archiveZip</code> the archiveZip
     */
    private static String archiveZip = "test.zip";

    /**
     * <code>parser</code> the parser
     */
    private static TikaParseur parser = new TikaParseur();

    /**
     * <code>typeMime</code> the typeMime
     */
    private static TypeMime typeMime;

    /**
     * @param file
     *            String
     */
    public void genericParseAperture(final String file) {

        try {
            // Recuperation fichier
            final URL fichier =
                        Thread.currentThread().getContextClassLoader().getResource(ressources + "/" + file);
            if (fichier == null) {
                throw new IOException("Le fichier n'a pas été trouvé");
            }
            // Parse fichier
            typeMime = parser.parse(new File(fichier.toURI()));
        } catch (final Exception e) {
            // Gestion exception
            LOGGER.info("Exception : ", e);
            typeMime = new TypeMime();
            typeMime.setNom(e.getMessage());
        }
        LOGGER.info("TypeMime : {}", typeMime.getNom());
    }

    /*************************************************************************/
    /******* TESTS SUR FICHIERS DE TYPE IMAGE *********/
    /******* JPG, PNG, GIF (fixe et animé) ************/
    /*************************************************************************/

    /**
     * testJPG
     */
    @Test
    public void testJPG() {

        LOGGER.info("testing JPG image...");
        genericParseAperture(jpgFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.IMG_JPEG);
        Assert.assertTrue(typeMime.isImage());
        LOGGER.info("");
    }

    /**
     * testPNG
     */
    @Test
    public void testPNG() {

        LOGGER.info("testing PNG image...");
        genericParseAperture(pngFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.IMG_PNG);
        Assert.assertTrue(typeMime.isImage());
        LOGGER.info("");
    }

    /**
     * testGIFNonAnime
     */
    @Test
    public void testGIFNonAnime() {

        LOGGER.info("testing GIF image non anime...");
        genericParseAperture(gifFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.IMG_GIF);
        Assert.assertTrue(typeMime.isImage());
        LOGGER.info("");
    }

    /**
     * testGIF
     */
    @Test
    public void testGIFAnime() {

        LOGGER.info("testing GIF image anime...");
        genericParseAperture(gifAnimeFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.IMG_GIF);
        Assert.assertTrue(typeMime.isImage());
        LOGGER.info("");
    }

    /*************************************************************************/
    /******* TESTS SUR FICHIERS DE TYPE TEXTE *********/
    /******* TXT, RTF, PDF ****************************/
    /*************************************************************************/

    /**
     * testText
     */
    @Test
    public void testText() {

        LOGGER.info("testing Text File...");
        genericParseAperture(textFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.TXT_TEXT);
        Assert.assertTrue(typeMime.isTextFile());
        LOGGER.info("");
    }

    /**
     * testRTF
     */
    @Test
    public void testRTF() {

        LOGGER.info("testing RTF...");
        genericParseAperture(rtfFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.TXT_RTF);
        Assert.assertTrue(typeMime.isTextFile());
        LOGGER.info("");
    }

    /**
     * testPDF
     */
    @Test
    public void testPDF() {

        LOGGER.info("testing PDF...");
        genericParseAperture(pdfFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.DOC_PDF);
        Assert.assertTrue(typeMime.isPDFFile());
        LOGGER.info("");
    }

    /*************************************************************************/
    /******* TESTS SUR FICHIERS DE TYPE OFFICE *********/
    /******* EXCEL, WORD *******************************/
    /*************************************************************************/

    /**
     * testExcel2K
     */
    @Test
    public void testExcel2K() {

        LOGGER.info("testing Excel 2K...");
        genericParseAperture(excel2KFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.EXCEL_2K);
        Assert.assertTrue(typeMime.isExcelFile());
        LOGGER.info("");
    }

    /**
     * testExcel2007
     */
    @Test
    public void testExcel2007() {

        LOGGER.info("testing Excel 2007...");
        genericParseAperture(excel2007File);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.EXCEL_2007);
        Assert.assertTrue(typeMime.isExcelFile());
        LOGGER.info("");
    }

    /**
     * testWord95
     */
    @Test
    public void testWord95() {

        LOGGER.info("testing Word 95...");
        genericParseAperture(word95File);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.WORD_95);
        Assert.assertTrue(typeMime.isWordFile());
        LOGGER.info("");
    }

    /**
     * testWord2K
     */
    @Test
    public void testWord2K() {

        LOGGER.info("testing Word 2K...");
        genericParseAperture(word2KFile);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.WORD_2K);
        Assert.assertTrue(typeMime.isWordFile());
        LOGGER.info("");
    }

    /**
     * testWord2007
     */
    @Test
    public void testWord2007() {

        LOGGER.info("testing Word 2007...");
        genericParseAperture(word2007File);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.WORD_2007);
        Assert.assertTrue(typeMime.isWordFile());
        LOGGER.info("");
    }

    /**
     * testOpenOfficeSheet
     */
    @Test
    public void testOpenOfficeSheet() {

        LOGGER.info("testing Open Office Sheet...");
        genericParseAperture(openOfficeSheet);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.OO_SHEET);
        Assert.assertTrue(typeMime.isOpenOfficeFile() && typeMime.isOpenOfficeSheetFile());
        LOGGER.info("");
    }

    /**
     * testOpenOfficeText
     */
    @Test
    public void testOpenOfficeText() {

        LOGGER.info("testing Open Office Text...");
        genericParseAperture(openOfficeText);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.OO_TEXT);
        Assert.assertTrue(typeMime.isOpenOfficeFile() && typeMime.isOpenOfficeTextFile());
        LOGGER.info("");
    }

    /*************************************************************************/
    /******* TESTS SUR ARCHIVE ************/
    /******* TAR, TAR GZ, GZ, ZIP *********/
    /*************************************************************************/

    /**
     * testArchiveTar
     */
    @Test
    public void testArchiveTar() {

        LOGGER.info("testing Archive Tar...");
        genericParseAperture(archiveTar);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.ARCH_G_TAR);
        Assert.assertTrue(typeMime.isArchiveFile());
        LOGGER.info("");
    }

    /**
     * testArchiveTarGz
     */
    @Test
    public void testArchiveTarGz() {

        LOGGER.info("testing Archive Tar Gz ...");
        genericParseAperture(archiveTarGz);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.ARCH_TAR_GZ);
        Assert.assertTrue(typeMime.isArchiveFile());
        LOGGER.info("");
    }

    /**
     * testArchiveGz
     */
    @Test
    public void testArchiveGz() {

        LOGGER.info("testing Archive Gz...");
        genericParseAperture(archiveGz);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.ARCH_GZIP);
        Assert.assertTrue(typeMime.isArchiveFile());
        LOGGER.info("");
    }

    /**
     * testArchiveZip
     */
    @Test
    public void testArchiveZip() {

        LOGGER.info("testing Archive Zip...");
        genericParseAperture(archiveZip);
        Assert.assertEquals(typeMime.getNom(), ConstantesTypeMime.ARCH_ZIP);
        Assert.assertTrue(typeMime.isArchiveFile());
        LOGGER.info("");
    }

}
