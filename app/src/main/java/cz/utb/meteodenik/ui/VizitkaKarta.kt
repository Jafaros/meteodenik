package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

@Composable
fun VizitkaKarta(
    jmeno: String,
    obor: String,
    email: String,
    modifier: Modifier = Modifier
) {
    val avatarPopis = stringResource(R.string.vizitka_avatar_popis, jmeno)
    val emailPopis = stringResource(R.string.vizitka_email_popis, email)
    val inicialy = jmeno.trim().split(Regex("\\s+"))
        .filter { it.isNotEmpty() }.take(2).joinToString("") { it.take(1).uppercase() }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clearAndSetSemantics { contentDescription = avatarPopis },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    inicialy,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(jmeno, style = MaterialTheme.typography.titleLarge,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(obor, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3, overflow = TextOverflow.Ellipsis)
                Text(email, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clearAndSetSemantics { contentDescription = emailPopis })
            }
        }
    }
}

@Preview(showBackground = true, locale = "cs", widthDp = 360)
@Preview(showBackground = true, locale = "cs", widthDp = 360,
    uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, locale = "cs", widthDp = 320, fontScale = 1.5f)
@Composable
private fun VizitkaKartaPreview() {
    MeteoDenikTheme {
        VizitkaKarta(
            stringResource(R.string.vizitka_jmeno),
            stringResource(R.string.vizitka_obor),
            stringResource(R.string.vizitka_email),
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
