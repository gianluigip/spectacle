package io.gianluigip.spectacle.specification

import io.gianluigip.spectacle.common.TransactionExecutor
import io.gianluigip.spectacle.specification.model.Feature
import io.gianluigip.spectacle.specification.model.FeatureName
import io.gianluigip.spectacle.specification.model.FeatureToDelete
import io.gianluigip.spectacle.specification.model.FeatureToUpsert
import io.gianluigip.spectacle.specification.model.Source
import io.gianluigip.spectacle.specification.model.SpecName
import io.gianluigip.spectacle.specification.model.SpecToUpsert
import io.gianluigip.spectacle.specification.model.Specification
import io.gianluigip.spectacle.specification.model.SpecificationsToUpdate
import io.gianluigip.spectacle.specification.model.Team
import io.gianluigip.spectacle.specification.model.TeamName
import io.gianluigip.spectacle.specification.model.TeamToDelete
import io.gianluigip.spectacle.specification.model.TeamToUpsert

class SpecificationProcessor(
    private val specRepo: SpecificationRepository,
    private val featureRepo: FeatureRepository,
    private val teamRepo: TeamRepository,
    private val transaction: TransactionExecutor
) {

    fun updateSpecifications(specificationsToUpdate: SpecificationsToUpdate) = transaction.execute {
        val source = specificationsToUpdate.source
        val component = specificationsToUpdate.component
        val specs = mutableMapOf<SpecName, SpecToUpsert>()
        val features = mutableListOf<FeatureToUpsert>()
        val teams = mutableMapOf<TeamName, TeamToUpsert>()
        specificationsToUpdate.features.forEach { featureFromSource ->
            val feature = FeatureToUpsert(
                name = featureFromSource.name, description = featureFromSource.description, source = source, component = component
            )
            features.add(feature)

            featureFromSource.specs.forEach { specificationFromSource ->
                val spec = specificationFromSource.run {
                    SpecToUpsert(
                        SpecName(name), feature.name, team, source, component, status, tags, steps, interactions
                    )
                }
                specs[spec.name] = spec
                teams[specificationFromSource.team] = TeamToUpsert(specificationFromSource.team, source, component)
            }
        }
        mergeSpecs(specs, source)
        mergeFeatures(features, source)
        mergeTeams(teams, source)
    }

    private fun mergeSpecs(specsToMerge: Map<SpecName, SpecToUpsert>, source: Source) {
        val specsToDelete = mutableListOf<Specification>()
        val specsToUpsert = mutableListOf<SpecToUpsert>()

        val existingSpecs = mutableMapOf<SpecName, Specification>()
        specRepo.findBy(sources = setOf(source)).forEach { spec ->
            if (!specsToMerge.containsKey(spec.name)) {
                specsToDelete.add(spec)
            } else {
                existingSpecs[spec.name] = spec
            }
        }

        specsToMerge.forEach { (name, spec) ->
            val existingSpec = existingSpecs[name]
            if (existingSpec == null) {
                specsToUpsert.add(spec)
            } else if (spec isNotEquals existingSpec) {
                specsToUpsert.add(spec)
            }
        }
        specRepo.delete(specsToDelete)
        specRepo.upsert(specsToUpsert)
    }

    private fun mergeFeatures(featuresToMerge: List<FeatureToUpsert>, source: Source) {
        val featuresNamesToMerge = featuresToMerge.map { it.name }.toSet()
        val featuresToDelete = mutableListOf<FeatureToDelete>()
        val featuresToUpsert = mutableListOf<FeatureToUpsert>()

        val existingFeatures = mutableMapOf<FeatureName, Feature>()
        featureRepo.findBySource(source).forEach { feature ->
            if (!featuresNamesToMerge.contains(feature.name)) {
                featuresToDelete.add(FeatureToDelete(feature.name, source))
            } else {
                existingFeatures[feature.name] = feature
            }
        }

        featuresToMerge.forEach { feature ->
            val existingFeature = existingFeatures[feature.name]
            if (existingFeature == null) {
                featuresToUpsert.add(feature)
            } else if (feature isNotEquals existingFeature) {
                featuresToUpsert.add(feature)
            }
        }
        featureRepo.delete(featuresToDelete)
        featureRepo.upsert(featuresToUpsert)
    }

    private fun mergeTeams(teamsToMerge: Map<TeamName, TeamToUpsert>, source: Source) {
        val teamsToDelete = mutableListOf<TeamToDelete>()
        val teamsToUpsert = mutableListOf<TeamToUpsert>()

        val existingTeams = mutableMapOf<TeamName, Team>()
        teamRepo.findBySource(source).forEach { team ->
            if (!teamsToMerge.contains(team.name)) {
                teamsToDelete.add(TeamToDelete(team.name, source))
            } else {
                existingTeams[team.name] = team
            }
        }

        teamsToMerge.forEach { (teamName, team) ->
            val existingFeature = existingTeams[teamName]
            if (existingFeature == null) {
                teamsToUpsert.add(team)
            }
        }
        teamRepo.delete(teamsToDelete)
        teamRepo.upsert(teamsToUpsert)
    }
}
