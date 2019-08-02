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
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;

/**
 * Initialise une SSLSocketFactory, avec le keystore fourni, et avec le AliasChooserKeyManager initialisé avec
 * l'alias du certificat client voulu Realiser à partir de la documentation MAEE : A3_DOC_Appel WS avec
 * Certificat Client.doc
 * 
 * @author MAEE - EffiTIC
 */
public class SSLSocketSpringFactory {

    /**
     * le keystore
     */
    private final transient File pKeyFile;

    /**
     * le mot de passe du keystore
     */
    private final transient String pKeyPassword;

    /**
     * l'alias du certificat client voulu
     */
    private final transient String certAlias;

    /**
     * Algorithm du keyManager
     */
    private final transient String algoKeyManager;

    /**
     * type du Keystore (JKS ou PKCS12)
     */
    private final transient String typeKeystore;

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
     */
    public SSLSocketSpringFactory(
                final File pKeyFile, final String pKeyPassword, final String certAlias,
                final String algoKeyManager, final String typeKeystore) {

        this.pKeyFile = pKeyFile;
        this.pKeyPassword = pKeyPassword;
        this.certAlias = certAlias;
        this.algoKeyManager = algoKeyManager;
        this.typeKeystore = typeKeystore;
    }

    /**
     * Initialise une SSLSocketFactory, avec le keystore fourni, et avec le AliasChooserKeyManager initialisé
     * avec l'alias du certificat client voulu
     * 
     * @return la SSLSocketFactory configuré
     * @throws GeneralSecurityException
     *             GeneralSecurityException
     * @throws IOException
     *             IOException
     */
    public SSLSocketFactory getObject() throws GeneralSecurityException, IOException {

        final SSLContext context = SSLContext.getInstance("TLSv1.2");
        // Initialisation le contexte SSL à l'aide du KeyStore
        context.init(this.getKeyManagers(), null, null);
        return context.getSocketFactory();
    }

    /**
     * Initialise un tableau de KeyMangers, avec le keystore fourni, et avec le AliasChooserKeyManager
     * initialisé avec l'alias du certificat client voulu
     * 
     * @return un tableau de KeyMangers
     * @throws GeneralSecurityException
     *             GeneralSecurityException
     * @throws IOException
     *             IOException
     */
    public KeyManager[] getKeyManagers() throws GeneralSecurityException, IOException {

        // Création de l'objet Keystore
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(this.algoKeyManager);
        final KeyStore keyStore = KeyStore.getInstance(this.typeKeystore); // ou
        // PKCS12
        InputStream keyInput = null;
        try {
            keyInput = new FileInputStream(this.pKeyFile);
            keyStore.load(keyInput, this.pKeyPassword.toCharArray());
        } finally {
            if (keyInput != null) {
                keyInput.close();
            }
        }
        keyManagerFactory.init(keyStore, this.pKeyPassword.toCharArray());

        // Remplacement des KeyManagers par défaut par des
        // AliasChooserKeyManager
        final KeyManager[] kms = keyManagerFactory.getKeyManagers();
        for (int i = 0; i < kms.length; i++) {
            if (kms[i] instanceof X509KeyManager) {
                kms[i] = new AliasChooserKeyManager((X509KeyManager) kms[i], this.certAlias);
            }
        }
        return kms;
    }
}

/**
 * Extension du X509ExtendedKeyManager pour permettre le choix d'un certificat précis du keystore à partir de
 * son alias
 */
class AliasChooserKeyManager extends X509ExtendedKeyManager {

    /**
     * baseKM : X509KeyManager
     */
    private final transient X509KeyManager baseKM;

    /**
     * alias :String
     */
    private final transient String alias;

    /**
     * AliasChooserKeyManager
     * 
     * @param keyManager
     *            X509KeyManager
     * @param alias
     *            String
     */
    public AliasChooserKeyManager(
                final X509KeyManager keyManager, final String alias) {

        super();
        this.baseKM = keyManager;
        this.alias = alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String chooseClientAlias(final String[] keyType, final Principal[] issuers, final Socket socket) {

        return this.alias;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String chooseServerAlias(final String keyStoreType, final Principal[] issuers, final Socket socket) {

        return this.baseKM.chooseServerAlias(keyStoreType, issuers, socket);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X509Certificate[] getCertificateChain(final String keyType) {

        return this.baseKM.getCertificateChain(this.alias);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getClientAliases(final String keyType, final Principal[] issuers) {

        return this.baseKM.getClientAliases(keyType, issuers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PrivateKey getPrivateKey(final String keyType) {

        return this.baseKM.getPrivateKey(this.alias);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getServerAliases(final String keyType, final Principal[] issuers) {

        return this.baseKM.getServerAliases(keyType, issuers);
    }

}
