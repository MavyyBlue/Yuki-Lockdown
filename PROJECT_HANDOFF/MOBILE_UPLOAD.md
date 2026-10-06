# Mobile update

Upload the complete yuki-lockdown-source-v1.8.0.zip at repository root. If not already updated in GitHub, replace .github/workflows/bootstrap.yml with the accompanying bootstrap.yml. Source bundles cannot overwrite their importer workflow. Require a successful run whose summary records the exact resulting source commit, then download yuki-lockdown-debug-APK and install over the existing app. Keep the original signing key and database; do not uninstall/reset.

The workflow verifies payload integrity and all files against the audited baseline or identical update before any overwrite. Existing owner edits are refused instead of silently erased. Matching expanded-source markers permit later builds without repeatedly reimporting the ZIP.

The mobile ZIP includes all application source, runtime artwork/resources, Gradle/tools/tests and documentation. Unused original image sheets under repository-root assets/ remain in the existing repository and are omitted from the overlay to fit GitHub mobile’s25MiB upload limit. Their provenance documents are included; no runtime resource or source code is omitted. Fresh application builds use the included app resources, not those archival sheets.
