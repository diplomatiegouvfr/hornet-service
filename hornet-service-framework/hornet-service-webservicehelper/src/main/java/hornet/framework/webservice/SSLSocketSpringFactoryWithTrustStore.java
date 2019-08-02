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
package hornet.framework.webservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author MEAE - Ministère de l'Europe et des Affaires étrangères
 *
 * Initialise une SSLSocketFactory, avec le keystore et le TrustStore fournis, et avec le
 * AliasChooserKeyManager initialisé avec l'alias du certificat client voulu en héritant de la classe
 * SSLSocketSpringFactory du package hornet.framework.webservice.
 */
public class SSLSocketSpringFactoryWithTrustStore extends SSLSocketSpringFactory {

    /**
     * le truststore
     */
    private final transient File pTrustStoreFile;

    /**
     * le mot de passe du trustStore
     */
    private final transient String pTrustStorePassword;

    /**
     * type du TrustStore (JKS ou PKCS12)
     */
    private final transient String typeTrusStore;

    /**
     * Algorithm du keyManager pour le TrustStore
     */
    private final transient String algoTrustStoreKeyManager;

    /**
     * @param pKeyFile
     *            le keystore
     * @param pKeyPassword
     *            le mot de passe du keystore
     * @param certAlias
     *            l'alias du certificat client voulu
     * @param algoKeyManager
     *            Algorithm du keyManager
     * @param typeKeystore
     *            JKS ou PKCS12
     * @param pTrustStoreFile
     *            le truststore
     * @param pTrustStorePassword
     *            le mot de passe du truststore
     * @param typeTrustStore
     *            (JKS ou PKCS12)
     * @param algoTrustStoreKeyManager
     *            Algorithm du gestionnaire de clé pour le TrustStore
     */
    public SSLSocketSpringFactoryWithTrustStore(
                final File pKeyFile, final String pKeyPassword, final String certAlias,
                final String algoKeyManager, final String typeKeystore, final File pTrustStoreFile,
                final String pTrustStorePassword, final String typeTrustStore,
                final String algoTrustStoreKeyManager) {

        // Mise à jour des attributs se rapportant au Keystore
        super(pKeyFile, pKeyPassword, certAlias, algoKeyManager, typeKeystore);
        // Mise à jour des attributs se rapportant au TrustStore
        this.pTrustStoreFile = pTrustStoreFile;
        this.pTrustStorePassword = pTrustStorePassword;
        this.typeTrusStore = typeTrustStore;
        this.algoTrustStoreKeyManager = algoTrustStoreKeyManager;

    }

    /**
     * Initialise une SSLSocketFactory, avec le keystore et le truststore fournis, et avec le
     * AliasChooserKeyManager initialisé avec l'alias du certificat client voulu
     * 
     * @return la SSLSocketFactory configuré
     * @throws GeneralSecurityException
     *             GeneralSecurityException
     * @throws IOException
     *             IOException
     */
    @Override
    public SSLSocketFactory getObject() throws GeneralSecurityException, IOException {

        // Remplacement des KeyManagers par défaut par des
        // AliasChooserKeyManager
        final KeyManager[] kms = super.getKeyManagers();

        // Remplacement du Truststore par défaut
        // Commençons par charger le Truststore à l'aide de la donnée membre contenant le chemim complet
        // Initialisons le truststore dont le format est défini par une propriété de l'objet
        final KeyStore trustStore = KeyStore.getInstance(this.typeTrusStore);
        InputStream trustStream = null;
        try {
            trustStream = new FileInputStream(this.pTrustStoreFile);
            // Chargeons le stream dans le trustore ; nous utilisons le password
            // du truststore pour lire le fichier
            trustStore.load(trustStream, this.pTrustStorePassword.toCharArray());
        } finally {
            // Fermons le TrustStore, nous n'en avons plus besoin
            if (trustStream != null) {
                trustStream.close();
            }
        }

        // initialize a trust manager factory with the trusted store
        // Initialisons le fournisseur de TrustStore à l'aide du magasin préparé
        final TrustManagerFactory trustFactory =
                    TrustManagerFactory.getInstance(this.algoTrustStoreKeyManager);
        trustFactory.init(trustStore);

        // Récupérons le TrustManager à l'aide du fournisseur
        final TrustManager[] trustManagers = trustFactory.getTrustManagers();

        final SSLContext context = SSLContext.getInstance("TLSv1.2");
        // Initialisation le contexte SSL à l'aide du KeyStore et du TrustStore
        context.init(kms, trustManagers, null);

        return context.getSocketFactory();
    }
}
