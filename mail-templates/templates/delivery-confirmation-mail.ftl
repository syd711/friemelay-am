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
	Guten Tag ${name},<br>
	<br>
	Ihre Bestellung wurde heute von uns bearbeitet und ist nun auf dem Weg zu Ihnen. Die Lieferzeit beträgt ca. 2-3 Werktage.<br>
    <br>
    Anbei erhalten Sie Ihre Rechnung sowie die AGB als PDF.<br>
	<br>
    Ihre Sendung wird durch DHL geliefert. <br>
    <br>
    <b>Ihre Bestellung im Überblick</b><br>
    <br>
    Bestelldatum: ${order.creationDate}<br>
    <br>


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
            <td colspan="5" align="right">Gesamt: ${order.formattedTotalPriceWithShipping} EUR</td>
            </tr>
            <tr>
            <td colspan="5" align="right"><hr></td>
            </tr>
            <tr>
            <td colspan="5" align="right"><p style="font-size:12px;">(Gem&auml;&szlig; §19 UstG (Kleinunternehmerregelung) entf&auml;llt die Umsatzsteuer.)</p></td>
            </tr>
            <tr>
            <td colspan="5" align="right"><p style="font-size:12px;">*Die angegebenen Versandkosten gelten nur f&uuml;r den Versand innerhalb Deutschlands. Bei Versand ins Ausland k&ouml;nnen die Versandkosten davon abweichen.</p></td>
            </tr>
        </table>
	<br>
    Es gelten unsere <a href="http://www.friemelay.de/pdf/agb.pdf">Allgemeinen Geschäftsbedingungen</a>, die Sie im Anhang in Textform erhalten.
    Zum Öffnen der Dokumente benötigen Sie den Adobe Reader,
    welchen Sie sich unter der URL: <a href="http://get.adobe.com/de/reader/">http://get.adobe.com/de/reader/</a> kostenlos herunterladen und installieren können.<br>
	<br>
	Eure Friemelay<br>
	(Thorsten Faust)<br>
	 </div>
	 </div>
	</body>
</html>

