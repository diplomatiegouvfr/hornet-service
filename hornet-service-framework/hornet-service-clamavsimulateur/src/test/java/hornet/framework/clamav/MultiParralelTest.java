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
package hornet.framework.clamav;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hornet.framework.clamav.bo.ClamAvResponse;
import hornet.framework.clamav.exception.ClamAVException;
import hornet.framework.clamav.service.ClamAVCheckService;

/**
 * Classe d'appel multi parallèle au service ClamAV Projet ELL036-TestClamAV.
 *
 * @author EffiTIC - SHE
 * @date 17 nov. 2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-appContext-test.xml"})
public class MultiParralelTest {

    /** Configuration. */
    @Autowired
    @Qualifier("configuration")
    private transient Configuration config;

    /** Service ClamAV. */
    @Autowired
    @Qualifier("clamServiceCheck")
    private transient ClamAVCheckService clamAVCheckService;

    /** Logger de la classe. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiParralelTest.class.getName());

    /** Logger CSV. */
    private static final Logger LOGGER_CSV = LoggerFactory.getLogger("CSV_LOGGER");

    /**
     * Thread d'appel à ClamAV Projet ELL036-TestClamAV.
     *
     * @author EffiTIC - SHE
     * @date 17 nov. 2011
     */
    private class ThreadIt extends Thread {

        /** Constante */
        private static final int CONST_8 = 8;

        /** Time to sleep for thread. */
        private static final int THREAD_SLEEP_TIME = 100;

        /** Numéro du thread. */
        private final transient int numThread;

        /** Liste des fichiers à traiter. */
        private final transient File[] listFichier;

        /** fichier sans virus. */
        private transient int nbFichierNoVirus;

        /** fichier vérolé. */
        private transient int nbFichierVirus;

        /** Absence de service. */
        private transient int nbFichierNoService;

        /** Timeout du service. */
        private transient int nbFichierTimeout;

        /**
         * Constructeur du thread.
         *
         * @param numThread
         *            le numéro de thread
         * @param aListFichier
         *            la liste des fichiers à traiter
         */
        public ThreadIt(
                    final int numThread, final File[] aListFichier) {

            super();
            this.numThread = numThread;
            listFichier = Arrays.asList(aListFichier).toArray(new File[0]);
            setName("Injecteur ClamAV n°" + numThread);
            nbFichierNoVirus = 0;
            nbFichierVirus = 0;
            nbFichierNoService = 0;
            nbFichierTimeout = 0;
        }

        /**
         * Statistique du service appelé.
         *
         * @param clamAvResponse
         *            la réponse ClamAV
         */
        public void stats(final ClamAvResponse clamAvResponse) {

            if (clamAvResponse != null) {

                if (ClamAvResponse.CategorieReponse.NO_VIRUS.equals(clamAvResponse.getCategorieReponse())) {
                    nbFichierNoVirus++;

                } else {
                    if (ClamAvResponse.CategorieReponse.VIRUS.equals(clamAvResponse.getCategorieReponse())) {
                        nbFichierVirus++;

                    } else {
                        if (ClamAvResponse.CategorieReponse.NO_SERVICE.equals(clamAvResponse
                            .getCategorieReponse())) {
                            nbFichierNoService++;

                        } else {
                            if (ClamAvResponse.CategorieReponse.TIMEOUT.equals(clamAvResponse
                                .getCategorieReponse())) {
                                nbFichierTimeout++;

                            }
                        }
                    }
                }
            }
        }

        /**
         * Ecriture des informations dans un fichier csv.
         *
         * @param noThread
         *            le numéro de thread
         * @param numFile
         *            le numéro de fichier
         * @param fileName
         *            le nom du fichier
         * @param fileSize
         *            la taille du fichier en Octer
         * @param time
         *            le temps d'exécution du service ClamAV
         */
        private void infocsv(final int noThread, final int numFile, final String fileName,
                    final long fileSize, final long time) {

            MultiParralelTest.LOGGER_CSV.info(String.format("%d;%d;%s;%d;%d\n", noThread, numFile, fileName,
                fileSize, time));

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {

            long startTime;
            long endTime;
            MultiParralelTest.LOGGER.warn(String.format("Envoi des %d fichiers", listFichier.length));
            ClamAvResponse clamAvResponse = null;
            for (int i = 0; i < listFichier.length; i++) {

                startTime = System.currentTimeMillis();

                clamAvResponse = analyseUnFichier(listFichier[i]);

                endTime = System.currentTimeMillis();

                infocsv(numThread, i + 1, listFichier[i].getName(),
                    Long.valueOf(listFichier[i].length() / CONST_8), Long.valueOf(endTime - startTime));

                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (final Exception e) {
                    MultiParralelTest.LOGGER.error("Problème de sleep après analyse", e);
                }

                stats(clamAvResponse);
            }
            MultiParralelTest.LOGGER
            .warn(
                "nbFichierNoVirus : {}, nbFichierVirus : {}, nbFichierNoService : {}, nbFichierTimeout : {} - Fin",
                nbFichierNoVirus, nbFichierVirus, nbFichierNoService,
                nbFichierTimeout);
        }
    }

    /**
     * Méthode d'analyse d'un fichier ClamAV.
     *
     * @param fichier
     *            le fichier à traiter
     * @return la réponse ClamAV
     */
    private ClamAvResponse analyseUnFichier(final File fichier) {

        ClamAvResponse ret = null;

        try {
            ret = clamAVCheckService.checkByStream(fichier);
        } catch (final ClamAVException e) {
            MultiParralelTest.LOGGER.error(e.getMessage());
        }

        if (ret != null) {
            MultiParralelTest.LOGGER.debug("Réponse Traitement fichier : {}", ret.getCategorieReponse());
        }

        return ret;
    }

    /**
     * Méthode de test du MultiParallele.
     *
     * @throws InterruptedException
     *             the interrupted exception
     * @throws URISyntaxException
     *             the URI syntax exception
     */
    @Test
    public void testMultiParrallel() throws InterruptedException, URISyntaxException {

        // Ignore le test si le fichier cible n'existe pas
        final URL url =
                    Thread.currentThread().getContextClassLoader()
                    .getResource(config.getCheminFichier());
        Assume.assumeNotNull(url);

        long startTime;
        long endTime;
        long refTime;
        Thread[] lesThreads;

        startTime = System.currentTimeMillis();

        analyseUnFichier(new File(url.toURI()));
        endTime = System.currentTimeMillis();
        refTime = endTime - startTime;
        // Configuration du nombre de thread à créer
        lesThreads = new Thread[config.getNbThread()];

        final URL urlChemin =
                    Thread.currentThread().getContextClassLoader().getResource(config.getChemin());

        final File directory = new File(urlChemin.toURI());

        if (directory.isFile()) {
            Assert.fail("le chemin de configuration n'est pas un répertoire");
        }

        final File[] fileList = directory.listFiles();

        Arrays.sort(fileList, new MPTComparator());

        int indexFile = 0;

        for (int i = 0; i < lesThreads.length; i++) {

            final File[] fileThread = new File[config.getNbFichier()];
            // constitution de la liste des fichiers à traiter
            for (int j = 0; j < config.getNbFichier(); j++) {

                // retour au début de la liste de fichier
                if (j + indexFile >= fileList.length) {
                    indexFile = -j;
                }

                // récupération du fichier
                fileThread[j] = fileList[j + indexFile];

                // sauvegarde du dernier indice de fichier
                if (j == config.getNbFichier() - 1) {
                    indexFile += j + 1;
                }
            }
            lesThreads[i] = new ThreadIt(i + 1, fileThread);
        }

        startTime = System.currentTimeMillis();
        for (final Thread lesThread : lesThreads) {
            lesThread.start();
        }

        for (final Thread lesThread : lesThreads) {
            lesThread.join();
        }

        endTime = System.currentTimeMillis();
        Assert.assertEquals(refTime, endTime - startTime, config.getTpsAcceptable());
        MultiParralelTest.LOGGER.info("nbThread : {}", config.getNbThread());
        MultiParralelTest.LOGGER.info("nbFichier par thread : {}", config.getNbFichier());
        MultiParralelTest.LOGGER.info("Total traité : {}",
            config.getNbThread() * config.getNbFichier());
        MultiParralelTest.LOGGER.info("Durée de traitement des threads : {}", endTime - startTime);
        MultiParralelTest.LOGGER.info("Temps de référence pour un fichier : {}", refTime);
    }

    /**
     * Classe de comparaison.
     *
     * @author MAE
     * @since 1.0 - 3 mai 2013
     */
    private class MPTComparator implements Comparator<File> {

        /**
         * Compare.
         *
         * @param o1
         *            the o1
         * @param o2
         *            the o2
         * @return the int
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final File o1, final File o2) {

            return o2.getName().compareTo(o1.getName());
        }
    }
}
