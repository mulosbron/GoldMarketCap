using System.Net;
using System.Net.Mail;
using Microsoft.Extensions.Configuration;

namespace GoldMarketCapAPI.Services
{
    public class EmailService
    {
        private readonly SmtpClient _smtpClient;
        private readonly string _fromEmail;

        public EmailService(IConfiguration configuration)
        {
            var smtpServer = configuration["EmailSettings:SMTPServer"] ?? throw new InvalidOperationException("SMTP server configuration is missing.");
            var smtpPortString = configuration["EmailSettings:SMTPPort"];
            var smtpPort = smtpPortString != null ? int.Parse(smtpPortString) : throw new InvalidOperationException("SMTP port configuration is missing.");
            var smtpUsername = configuration["EmailSettings:SMTPUsername"] ?? throw new InvalidOperationException("SMTP username configuration is missing.");
            var smtpPassword = configuration["EmailSettings:SMTPPassword"] ?? throw new InvalidOperationException("SMTP password configuration is missing.");
            _fromEmail = smtpUsername;

            _smtpClient = new SmtpClient(smtpServer)
            {
                Port = smtpPort,
                Credentials = new NetworkCredential(smtpUsername, smtpPassword),
                EnableSsl = true,
            };
        }

        public void SendPasswordResetEmail(string toEmail, string resetToken)
        {
            using var mailMessage = new MailMessage(_fromEmail, toEmail)
            {
                Subject = "Şifre Sıfırlama",
                Body = $"Şifre sıfırlama kodunuz: {resetToken}",
                IsBodyHtml = true
            };

            _smtpClient.Send(mailMessage);
        }
    }
}

/*
public void SendPasswordResetEmail(string toEmail, string resetToken)
{
    // Şifre sıfırlama URL'si. Bu URL'nin uygulamanızın ön yüzünde şifre sıfırlama sayfasına yönlendirilmesi gerekir.
    var resetUrl = $"https://yourdomain.com/reset-password?token={resetToken}";

    // E-posta gövdesini HTML kullanarak oluşturun.
    var body = $@"
<html>
<head>
  <style>
    body {{ font-family: 'Arial'; }}
    .button {{
      display: inline-block;
      padding: 10px 20px;
      font-size: 18px;
      cursor: pointer;
      text-align: center;
      text-decoration: none;
      outline: none;
      color: #fff;
      background-color: #4CAF50;
      border: none;
      border-radius: 15px;
      box-shadow: 0 9px #999;
    }}

    .button:hover {{background-color: #3e8e41}}

    .button:active {{
      background-color: #3e8e41;
      box-shadow: 0 5px #666;
      transform: translateY(4px);
    }}
  </style>
</head>
<body>
  <h2>Şifre Sıfırlama İsteği</h2>
  <p>Şifrenizi sıfırlamak için aşağıdaki butona tıklayın:</p>
  <a href='{resetUrl}' class='button'>Şifre Sıfırla</a>
  <p>Eğer bu isteği siz yapmadıysanız, bu e-postayı dikkate almayınız.</p>
</body>
</html>";

    using var mailMessage = new MailMessage(_fromEmail, toEmail)
    {
        Subject = "Şifre Sıfırlama İsteği",
        Body = body,
        IsBodyHtml = true // E-posta gövdesinin HTML olarak işlenmesi gerektiğini belirtir.
    };

    _smtpClient.Send(mailMessage);
}

*/
