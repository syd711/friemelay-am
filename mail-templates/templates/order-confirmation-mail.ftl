<html>
	<head>
		<title>${subject}</title>
	</head>
	<body style="font-family:Verdana;font-size:12px;">
	<div>
	<div style="align:center;width:90%;padding:10px;text-align:left;border: 0px solid #ccc;">
	<img src="http://www.friemelay.de/img/logo-small.jpg" alt="friemelay-logo" /><br>
	<br>
	Schillerstra&szlig;e 43<br>
	59555 Lippstadt<br>
	<br>
	<br>
	<b>Bestellbestätigung / Rechnung Nr. ${order.formattedId} vom ${order.formattedCreationDate}</b><br>
	<br>
	<br>
	Guten Tag,<br>
	<br>
	vielen Dank für Ihre Bestellung vom  bei der <b>Friemelay</b>!<br>
	<br>
	Die von Ihnen bestellten Waren sind	vorrätig, somit nehmen wir hiermit Ihre Bestellung an und werden sie unverzüglich nach Erhalt der Begleichung der Rechnungssumme an die ausliefern.<br>
	<br>
	Ihre Bestellung geht an:<br>
	<br>
  	${address.firstname.value} ${address.lastname.value}<br>
  	<#if address.company?has_content>
  	    ${address.company.value}<br>
  	</#if>
  	<#if address.additional?has_content>
  	    ${address.additional.value}<br>
    </#if>
  	${address.street.value}<br>
  	${address.zip.value}<br>
  	${address.city.value}<br>
  	<#if address.country?has_content>
	    ${address.country.value}<br>
    </#if>
	 <br>

	Versandart: Standardversand DHL<br>
	<br>
	<b>Einzeilheiten Ihrer Bestellung:</b><br>
      <br>
         <table border="0" cellpadding="6" cellspacing="0" style="font-family:Verdana;font-size:14px;">
          <tr>
            <td></td>
            <td><b>Name</b></td>
            <td><b>Anzahl</b></td>
            <td><b>Einzelpreis</b></td>
            <td align="right"><b>Preis</b></td>
          </tr>

             <#list order.orderItems as item>
                <tr>
                    <td><a href="${item.url}">
                        <img style="border: 1px solid #d5d5d5;padding:3px;" width="120" src="${item.imageUrl}" alt="${item.productDescription}"></a>
                    </td>
                    <td><a href="${item.url}"><b>${item.productDescription}</b></a></td>
                    <td>${item.amount.value}</td>
                    <td>${item.price} Euro</td>
                    <td align="right">${item.formattedTotalPrice} EUR</td>
                </tr>
            </#list>
            <tr>
                <td colspan="5" align="right"><hr></td>
            </tr>
            <tr>
                <td colspan="5" align="right">Zwischensumme: ${order.formattedTotalPrice} EUR</td>
            </tr>
            <tr>
                <td colspan="5" align="right">Versandkosten*: ${order.formattedShippingCosts} EUR</td>
            </tr>
            <tr>
            <td colspan="5" align="right"><hr></td>
            </tr>
            <tr>
            <td colspan="5" align="right"><b>Gesamt: ${order.formattedTotalPriceWithShipping} EUR</b></td>
            </tr>
            <tr>
            <td colspan="5" align="right"><hr></td>
            </tr>
            <tr>
            <td colspan="5" align="right"><p style="font-size:10px;">(Gem&auml;&szlig; §19 UstG (Kleinunternehmerregelung) entf&auml;llt die Umsatzsteuer.)</p></td>
            </tr>
            <tr>
            <td colspan="5" align="right"><p style="font-size:10px;">*Die angegebenen Versandkosten gelten nur f&uuml;r den Versand innerhalb Deutschlands. Bei Versand ins Ausland k&ouml;nnen die Versandkosten davon abweichen.</p></td>
            </tr>
        </table>
	<br>
	<br>
	Bitte richten Sie Ihre Überweisungen mit dem Verwendungzweck "Bestellung ${order.formattedId}" an die:<br>
	<br>
    Volksbank Beckum-Lippstadt<br>
    Konto- Nummer:<br>
    BLZ: ${blz}<br>
    IBAN: ${iban}<br>
    Inhaber: Thorsten Faust<br>
    <br>

    Wir werden Sie umgehend benachrichtigen, sobald die von Ihnen bei uns bestellten Waren versandt wurden.<br>
    <br>
    Die Lieferung erfolgt unverzüglich nach Zahlungseingang. Bitte beachten Sie, dass Warensendungen bis zu 7 Werktage auf dem "Postweg" unterwegs sein können.<br>
    <br>
    Sollte der o.g. Betrag innerhalb der 10 Tages-Frist nicht überwiesen werden, gilt ihre Bestellung als ungültig.<br>
	<br>
    Unsere AGB finden Sie auf der unserer Homepage unter <a href="http://www.friemelay.de">www.friemelay.de.</a><br>
    <br>
    Wir wünschen Ihnen viel Spaß mit unseren Waren und laden Sie gerne wieder dazu ein in unserem Sortiment zu stöbern.<br>
	<br>


    Mit freundlichen Gr&uuml;&szlig;en,<br>
	<br>
	Eure Friemelay<br>
	(Thorsten Faust)<br>
    <br>
	<br>
	<p style="font-size:10px;">
	Impressum:<br>
    <a href="http://www.friemelay.de">http://www.friemelay.de.</a><br>
    Schillerstraße 43<br>
    59555 Lippstadt<br>
    Amtsgericht Lippstadt<br>
    <br>
    Geschäftsführer:<br>
    Thorsten Faust<br>
    <br>
    Sitz der Gesellschaft: Lippstadt<br>
    Ust-IdNr. DE ${ustid}<br>
    </p>

	 </div>
	 </div>
	</body>
</html>

