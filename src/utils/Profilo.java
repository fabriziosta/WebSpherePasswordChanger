package utils;

import javax.swing.JCheckBox;

public class Profilo {
	private String tipoInstallazione; //x86 || x64
	private String versioneWAS; //base_v7 || base_v85 || base_v9
	private JCheckBox checkboxAssociataProfilo;
	private String nomeProfilo; //nome definito dall'utente
	
	public Profilo(String versione, JCheckBox checkbox, String nomeProfilo, String tipoInstallazione) {
		this.versioneWAS = versione;
		this.checkboxAssociataProfilo = checkbox;
		this.nomeProfilo = nomeProfilo;
		this.tipoInstallazione = tipoInstallazione;
	}

	public String getVersioneWAS() {
		return versioneWAS;
	}

	public void setVersioneWAS(String versioneWAS) {
		this.versioneWAS = versioneWAS;
	}

	public JCheckBox getCheckboxAssociataProfilo() {
		return checkboxAssociataProfilo;
	}

	public void setCheckboxAssociataProfilo(JCheckBox checkboxAssociataProfilo) {
		this.checkboxAssociataProfilo = checkboxAssociataProfilo;
	}

	public String getNomeProfilo() {
		return nomeProfilo;
	}

	public void setNomeProfilo(String nomeProfilo) {
		this.nomeProfilo = nomeProfilo;
	}

	public String getTipoInstallazione() {
		return tipoInstallazione;
	}

	public void setTipoInstallazione(String tipoInstallazione) {
		this.tipoInstallazione = tipoInstallazione;
	}
	
	
}
